package com.romazal.ecommerce.product_movement;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductMovementResponse(
        UUID productMovementId,
        UUID productId,
        Double quantity,
        MovementType movementType,
        LocalDateTime createdDate
) {
}
