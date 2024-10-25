package com.romazal.ecommerce.shipping;

import com.romazal.ecommerce.exception.ShippingNotFoundException;
import com.romazal.ecommerce.order.OrderClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.romazal.ecommerce.shipping.DeliveryStatus.*;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final ShippingRepository repository;
    private final ShippingMapper mapper;
    private final OrderClient orderClient;

    public UUID createShipping(ShippingCreationRequest shippingCreationRequest) {
        var shipping = mapper.toShipping(shippingCreationRequest);

        return repository.save(shipping).getShippingId();
    }

    public UUID confirmShipping(ShippingConfirmRequest shippingConfirmRequest) {
        var shipping = repository.findById(shippingConfirmRequest.shippingId())
                .orElseThrow(() -> new ShippingNotFoundException(
                        format("No shipping found with the provided ID:: %s", shippingConfirmRequest.shippingId())
                ));

        if (shipping.getDeliveryStatus() != PENDING) {
            throw new IllegalStateException(
                    format("Cannot confirm the shipping, the shipping is no longer pending, current delivery status:: %s", shipping.getDeliveryStatus())
            );
        }

        shipping.setDeliveryStatus(SHIPPING);

        orderClient.setOrderStatusToShipping(shipping.getOrderId());

        return repository.save(shipping).getShippingId();
    }

    public UUID successShipping(UUID shippingId) {
        var shipping = repository.findById(shippingId)
                .orElseThrow(() -> new ShippingNotFoundException(
                        format("No shipping found with the provided ID:: %s", shippingId)
                ));

        if (shipping.getDeliveryStatus() != SHIPPING) {
            throw new IllegalStateException(
                    format("Cannot conclude the shipping, the shipping's delivery is not in shipping status, current delivery status:: %s", shipping.getDeliveryStatus())
            );
        }

        shipping.setDeliveryStatus(DELIVERED);

        orderClient.setOrderStatusToDelivered(shipping.getOrderId());

        return repository.save(shipping).getShippingId();
    }

    public UUID failShipping(UUID shippingId) {
        var shipping = repository.findById(shippingId)
                .orElseThrow(() -> new ShippingNotFoundException(
                        format("No shipping found with the provided ID:: %s", shippingId)
                ));

        if (shipping.getDeliveryStatus() == FAILED) {
            throw new IllegalStateException(
                    format("Cannot fail the shipping, the shipping is already failed, current delivery status:: %s", shipping.getDeliveryStatus())
            );
        }

        if (shipping.getDeliveryStatus() == DELIVERED) {
            throw new IllegalStateException(
                    format("Cannot fail the shipping, the shipping is already successfully delivered, current delivery status:: %s", shipping.getDeliveryStatus())
            );
        }

        shipping.setDeliveryStatus(FAILED);

        orderClient.cancelOrder(shipping.getOrderId());

        return repository.save(shipping).getShippingId();
    }
}
