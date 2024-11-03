package com.romazal.ecommerce.kafka.shipment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShipmentShippedNotification(
        UUID shipmentId,
        UUID orderId,
        String customerEmail,
        String customerName,
        String trackingNumber,
        String logisticsProvider,
        LocalDateTime shippedDate,
        LocalDateTime estimatedDeliveryDate
) {
}
