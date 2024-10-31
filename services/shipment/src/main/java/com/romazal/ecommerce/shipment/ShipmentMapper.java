package com.romazal.ecommerce.shipment;

import org.springframework.stereotype.Service;

@Service
public class ShipmentMapper {
    public Shipment toShipping(ShipmentConfirmRequest shipmentConfirmRequest) {
        if (shipmentConfirmRequest == null) return null;

        return new Shipment().builder()
                .trackingNumber(shipmentConfirmRequest.trackingNumber())
                .logisticsProvider(shipmentConfirmRequest.logisticsProvider())
                .estimatedDeliveryDate(shipmentConfirmRequest.estimatedDeliveryDate())
                .build();
    }

    public Shipment toShipping(ShipmentCreationRequest shipmentCreationRequest) {
        if (shipmentCreationRequest == null) return null;

        return new Shipment().builder()
                .orderId(shipmentCreationRequest.orderId())
                .build();
    }
}
