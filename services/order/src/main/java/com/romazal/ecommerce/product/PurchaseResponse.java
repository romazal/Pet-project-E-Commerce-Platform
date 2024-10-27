package com.romazal.ecommerce.product;

import java.math.BigDecimal;
import java.util.UUID;

public record PurchaseResponse(
        UUID productId,
        String name,
        String description,
        BigDecimal price,
        Double stockQuantity
) {
}
