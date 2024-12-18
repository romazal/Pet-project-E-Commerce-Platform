package com.romazal.ecommerce.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID paymentId,
        UUID orderId,
        BigDecimal totalAmount,
        String customerEmail,
        String customerName,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
