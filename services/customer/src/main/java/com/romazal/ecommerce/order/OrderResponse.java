package com.romazal.ecommerce.order;

import com.romazal.ecommerce.payment.PaymentMethod;
import com.romazal.ecommerce.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        Long customerId,
        BigDecimal totalAmount,
        PaymentStatus paymentStatus,
        OrderStatus orderStatus,
        PaymentMethod paymentMethod,
        String shippingAddress,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
