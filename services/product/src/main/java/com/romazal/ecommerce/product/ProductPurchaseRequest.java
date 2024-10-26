package com.romazal.ecommerce.product;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductPurchaseRequest(
        @NotNull(message = "Product id is mandatory")
        UUID productId,
        @NotNull(message = "Product quantity is mandatory")
        Double quantity
) {
}
