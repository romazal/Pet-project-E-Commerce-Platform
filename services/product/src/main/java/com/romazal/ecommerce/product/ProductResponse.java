package com.romazal.ecommerce.product;

import com.romazal.ecommerce.category.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID productId,
        Long vendorId,
        String name,
        String description,
        BigDecimal price,
        Double stockQuantity,
        Double thresholdQuantity,
        String sku,
        LocalDateTime createdDate,
        Category categoryId,
        String categoryName,
        String categoryDescription
) {
}
