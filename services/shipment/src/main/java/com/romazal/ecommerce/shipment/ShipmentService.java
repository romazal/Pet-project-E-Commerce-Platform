package com.romazal.ecommerce.shipment;

import com.romazal.ecommerce.exception.ShippingNotFoundException;
import com.romazal.ecommerce.kafka.notification.NotificationKafkaTemplate;
import com.romazal.ecommerce.kafka.notification.ShipmentDeliveredNotification;
import com.romazal.ecommerce.kafka.notification.ShipmentShippedNotification;
import com.romazal.ecommerce.order.OrderClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.romazal.ecommerce.shipment.DeliveryStatus.*;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository repository;
    private final ShipmentMapper mapper;
    private final OrderClient orderClient;
    private final NotificationKafkaTemplate notificationKafkaTemplate;

    public UUID createShipping(ShipmentCreationRequest shipmentCreationRequest) {
        if(repository.findFirstByOrderIdAndDeliveryStatusNot(shipmentCreationRequest.orderId(), FAILED).isPresent()) {
            throw new IllegalArgumentException(
                    format("Shipment already exists for order ID:: %s", shipmentCreationRequest.orderId())
            );
        }

        var shipment = mapper.toShipping(shipmentCreationRequest);

        return repository.save(shipment).getShipmentId();
    }

    public UUID confirmShipping(ShipmentConfirmRequest shipmentConfirmRequest) {
        var shipment = repository.findById(shipmentConfirmRequest.shippingId())
                .orElseThrow(() -> new ShippingNotFoundException(
                        format("No shipping found with the provided ID:: %s", shipmentConfirmRequest.shippingId())
                ));

        if (shipment.getDeliveryStatus() != PENDING) {
            throw new IllegalStateException(
                    format("Cannot confirm the shipping, the shipping is no longer pending, current delivery status:: %s", shipment.getDeliveryStatus())
            );
        }

        shipment.setDeliveryStatus(SHIPPING);
        shipment.setShippedDate(LocalDateTime.now());
        shipment.setTrackingNumber(shipmentConfirmRequest.trackingNumber());
        shipment.setLogisticsProvider(shipmentConfirmRequest.logisticsProvider());
        shipment.setEstimatedDeliveryDate(shipmentConfirmRequest.estimatedDeliveryDate());

        orderClient.setOrderStatusToShipped(shipment.getOrderId());

        notificationKafkaTemplate.sendShipmentShippedNotification(
                new ShipmentShippedNotification(
                        shipment.getShipmentId(),
                        shipment.getOrderId(),
                        shipment.getCustomerEmail(),
                        shipment.getCustomerName(),
                        shipment.getTrackingNumber(),
                        shipment.getLogisticsProvider(),
                        shipment.getShippedDate(),
                        shipment.getEstimatedDeliveryDate()
                )
        );

        return repository.save(shipment).getShipmentId();
    }

    public UUID successShipping(UUID shipmentId) {
        var shipment = repository.findById(shipmentId)
                .orElseThrow(() -> new ShippingNotFoundException(
                        format("No shipping found with the provided ID:: %s", shipmentId)
                ));

        if (shipment.getDeliveryStatus() != SHIPPING) {
            throw new IllegalStateException(
                    format("Cannot conclude the shipping, the shipping's delivery is not in shipping status, current delivery status:: %s", shipment.getDeliveryStatus())
            );
        }

        shipment.setDeliveryStatus(DELIVERED);
        shipment.setDeliveredDate(LocalDateTime.now());

        orderClient.setOrderStatusToDelivered(shipment.getOrderId());

        notificationKafkaTemplate.sendShipmentDeliveredNotification(
                new ShipmentDeliveredNotification(
                        shipment.getShipmentId(),
                        shipment.getOrderId(),
                        shipment.getCustomerEmail(),
                        shipment.getCustomerName(),
                        shipment.getTrackingNumber(),
                        shipment.getLogisticsProvider(),
                        shipment.getDeliveredDate()
                )
        );

        return repository.save(shipment).getShipmentId();
    }

    public UUID failShipping(UUID shipmentId) {
        var shipment = repository.findById(shipmentId)
                .orElseThrow(() -> new ShippingNotFoundException(
                        format("No shipping found with the provided ID:: %s", shipmentId)
                ));

        if (shipment.getDeliveryStatus() == FAILED) {
            throw new IllegalStateException(
                    format("Cannot fail the shipping, the shipping is already failed, current delivery status:: %s", shipment.getDeliveryStatus())
            );
        }

        if (shipment.getDeliveryStatus() == DELIVERED) {
            throw new IllegalStateException(
                    format("Cannot fail the shipping, the shipping is already successfully delivered, current delivery status:: %s", shipment.getDeliveryStatus())
            );
        }

        shipment.setDeliveryStatus(FAILED);

        return repository.save(shipment).getShipmentId();
    }

    public ShipmentResponse getShipmentByShipmentId(UUID shipmentId) {
        return repository.findById(shipmentId).map(mapper::toShipmentResponse)
                .orElseThrow(
                        () -> new ShippingNotFoundException(
                                format("No shipping found with the provided ID:: %s", shipmentId)
                        )
                );
    }

    public ShipmentResponse getShipmentByTrackingNumber(String trackingNumber) {
        return repository.findByTrackingNumber(trackingNumber).map(mapper::toShipmentResponse)
                .orElseThrow(
                        () -> new ShippingNotFoundException(
                                format("No shipping found with the provided tracking number:: %s", trackingNumber)
                        )
                );
    }

    public List<ShipmentResponse> getAllShipments() {
        return repository.findAll()
                .stream()
                .map(mapper::toShipmentResponse)
                .toList();
    }

    public void refundShipmentByOrderId(UUID orderId) {
        Shipment shipment = repository.findFirstByOrderIdAndDeliveryStatusNot(orderId, FAILED)
                .orElseThrow(() -> new ShippingNotFoundException(
                        format("No shipping found with the provided ID:: %s", orderId)
                ));

        shipment.setDeliveryStatus(REFUNDED);

        repository.save(shipment);

    }
}
