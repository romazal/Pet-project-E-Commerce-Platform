package com.romazal.ecommerce.kafka.notification;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRefundNotification(
        UUID paymentId,
        UUID orderId,
        BigDecimal totalAmount,
        String customerEmail,
        String customerName
) {
}
