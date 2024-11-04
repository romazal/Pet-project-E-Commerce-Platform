package com.romazal.ecommerce.kafka.notification;

import com.romazal.ecommerce.product.PurchaseRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderConfirmationNotification(
        UUID orderId,
        String customerEmail,
        String customerName,
        BigDecimal totalAmount,
        List<PurchaseRequest> orderItems
) {
}
