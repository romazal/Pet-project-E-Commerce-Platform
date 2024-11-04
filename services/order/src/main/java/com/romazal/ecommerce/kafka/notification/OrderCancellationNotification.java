package com.romazal.ecommerce.kafka.notification;

import com.romazal.ecommerce.product.PurchaseRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderCancellationNotification(
        UUID orderId,
        String customerEmail,
        String customerName,
        BigDecimal totalAmount,
        List<PurchaseRequest> orderItems,
        LocalDateTime createdDate
) {
}
