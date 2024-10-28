package com.romazal.ecommerce.kafka.notification;

import java.util.UUID;

public record ProductThresholdNotification(
        UUID productId,
        String productName,
        String sku,
        Double stockQuantity,
        Double thresholdQuantity,
        String storeName,
        String storeEmail
) {
}
