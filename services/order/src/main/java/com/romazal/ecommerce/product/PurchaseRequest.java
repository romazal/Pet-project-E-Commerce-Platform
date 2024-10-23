package com.romazal.ecommerce.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record PurchaseRequest (
        @NotNull(message = "Product ID is required.")
        UUID productId,

        @NotNull(message = "Quantity is required.")
        @Positive(message = "Quantity must be greater than zero.")
        Double quantity,

        @NotNull(message = "Unit price is required.")
        @DecimalMin(value = "0.0", inclusive = false,
                message = "Unit price must be greater than zero.")
        BigDecimal unitPrice
){
}
