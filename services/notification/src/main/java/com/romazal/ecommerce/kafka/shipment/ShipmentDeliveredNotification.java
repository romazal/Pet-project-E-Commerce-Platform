package com.romazal.ecommerce.kafka.shipment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShipmentDeliveredNotification(
        UUID shipmentId,
        UUID orderId,
        String customerEmail,
        String customerName,
        String trackingNumber,
        String logisticsProvider,
        LocalDateTime deliveredDate
) {
}
