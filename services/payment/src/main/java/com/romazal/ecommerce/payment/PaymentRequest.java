package com.romazal.ecommerce.payment;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(

        @NotNull(message = "Order ID is required")
        @Positive(message = "Order ID must be positive")
        UUID orderId,

        @NotNull(message = "Customer Email is required")
        String customerEmail,

        @NotNull(message = "Customer Name is required")
        String customerName,

        @NotNull(message = "Total amount is required")
        @DecimalMin(value = "0.0", inclusive = false,
                message = "Total amount must be greater than 0")
        BigDecimal totalAmount,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod

) {
}
