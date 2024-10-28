package com.romazal.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ProductRestockRequest(
        @NotNull(message = "Vendor ID is required")
        @Positive(message = "Vendor ID must be positive")
        UUID productId,

        @NotNull(message = "Restock quantity is required")
        @Positive(message = "Restock quantity must be positive")
        Double restockQuantity
) {
}
