package com.romazal.ecommerce.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(
        UUID productId,

        @NotNull(message = "Vendor ID is required")
        @Positive(message = "Vendor ID must be positive")
        Long vendorId,

        @NotNull(message = "Product name is required")
        @NotBlank(message = "Product name cannot be blank")
        @Size(min = 2, max = 100,
                message = "Product name must be between 2 and 100 characters")
        String name,

        @NotNull(message = "Description is required")
        @Size(max = 1000,
                message = "Description cannot exceed 1000 characters")
        String description,


        @NotNull(message = "Price is required.")
        @DecimalMin(value = "0.0", inclusive = false,
                message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Category is required")
        UUID categoryId,

        @Min(value = 0,
                message = "Stock quantity must be at least 0")
        Double stockQuantity,

        @Min(value = 0,
                message = "Threshold quantity must be at least 0")
        Double thresholdQuantity,

        @Size(max = 50,
                message = "SKU cannot exceed 50 characters")
        String sku

) {
}
