package com.romazal.ecommerce.order;

import com.romazal.ecommerce.payment.PaymentMethod;
import com.romazal.ecommerce.payment.PaymentStatus;
import com.romazal.ecommerce.product.PurchaseRequest;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderUpdateRequest(
        UUID orderId,


        @Positive(message = "Customer ID must be positive")
        Long customerId,

        @DecimalMin(value = "0.0", inclusive = false,
                message = "Total amount must be greater than 0")
        BigDecimal totalAmount,

        PaymentStatus paymentStatus,

        OrderStatus orderStatus,

        PaymentMethod paymentMethod,

        @NotBlank(message = "Shipping address cannot be blank")
        @Size(max = 255,
                message = "Shipping address cannot exceed 255 characters")
        String shippingAddress,

        @Size(min = 1,
                message = "There must be at least one order item")
        List<PurchaseRequest> orderItems
) {
}
