package com.romazal.ecommerce.shipment;

import org.springframework.stereotype.Service;

@Service
public class ShipmentMapper {


    public Shipment toShipping(ShipmentCreationRequest shipmentCreationRequest) {
        if (shipmentCreationRequest == null) return null;

        return new Shipment().builder()
                .orderId(shipmentCreationRequest.orderId())
                .customerEmail(shipmentCreationRequest.customerEmail())
                .customerName(shipmentCreationRequest.customerName())
                .build();
    }

    public ShipmentResponse toShipmentResponse(Shipment shipment) {
        if (shipment == null) return null;

        return new ShipmentResponse(
                shipment.getShipmentId(),
                shipment.getOrderId(),
                shipment.getCustomerEmail(),
                shipment.getCustomerName(),
                shipment.getTrackingNumber(),
                shipment.getLogisticsProvider(),
                shipment.getDeliveryStatus(),
                shipment.getShippedDate(),
                shipment.getEstimatedDeliveryDate(),
                shipment.getDeliveredDate(),
                shipment.getCreatedDate(),
                shipment.getLastModifiedDate()
        );
    }
}
