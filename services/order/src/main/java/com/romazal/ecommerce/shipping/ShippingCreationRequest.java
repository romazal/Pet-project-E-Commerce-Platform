package com.romazal.ecommerce.shipping;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ShippingCreationRequest(
        @NotNull(message = "Order ID is required")
        @Positive(message = "Order ID must be positive")
        UUID orderId
) {
}
