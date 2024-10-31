package com.romazal.ecommerce.order_item;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID orderItemId,
        UUID order,
        UUID productId,
        Double quantity,
        BigDecimal unitPrice
) {
}
