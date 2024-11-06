package com.romazal.ecommerce.shipment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShipmentResponse(
        UUID shipmentId,
        UUID orderId,
        String customerEmail,
        String customerName,
        String trackingNumber,
        String logisticsProvider,
        DeliveryStatus deliveryStatus,
        LocalDateTime shippedDate,
        LocalDateTime estimatedDeliveryDate,
        LocalDateTime deliveredDate,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
