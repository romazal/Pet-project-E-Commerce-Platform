package com.romazal.ecommerce.shipping;

import org.springframework.stereotype.Service;

@Service
public class ShippingMapper {
    public Shipping toShipping(ShippingConfirmRequest shippingConfirmRequest) {
        if (shippingConfirmRequest == null) return null;

        return new Shipping().builder()
                .trackingNumber(shippingConfirmRequest.trackingNumber())
                .logisticsProvider(shippingConfirmRequest.logisticsProvider())
                .estimatedDeliveryDate(shippingConfirmRequest.estimatedDeliveryDate())
                .build();
    }

    public Shipping toShipping(ShippingCreationRequest shippingCreationRequest) {
        if (shippingCreationRequest == null) return null;

        return new Shipping().builder()
                .orderId(shippingCreationRequest.orderId())
                .build();
    }
}
