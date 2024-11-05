package com.romazal.ecommerce.kafka.payment;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentConfirmationNotification(
        UUID paymentId,
        UUID orderId,
        BigDecimal totalAmount,
        String customerEmail,
        String customerName,
        PaymentMethod paymentMethod
) {
}
