package com.romazal.ecommerce.order;

import com.romazal.ecommerce.orderItem.OrderItem;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderRequest(
        UUID orderId,

        @NotNull(message = "Customer ID is required")
        @Positive(message = "Customer ID must be positive")
        Long customerId,

        @NotNull(message = "Total amount is required")
        @DecimalMin(value = "0.0", inclusive = false,
                message = "Total amount must be greater than 0")
        BigDecimal totalAmount,

        @NotNull(message = "Payment status is required")
        PaymentStatus paymentStatus,

        @NotNull(message = "Order status is required")
        OrderStatus orderStatus,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        @NotNull(message = "Shipping address is required")
        @NotBlank(message = "Shipping address cannot be blank")
        @Size(max = 255,
                message = "Shipping address cannot exceed 255 characters")
        String shippingAddress,

        @NotNull(message = "Order items are required")
        @Size(min = 1,
                message = "There must be at least one order item")
        List<OrderItem> orderItems
) {
}
