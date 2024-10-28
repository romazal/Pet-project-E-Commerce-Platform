package com.romazal.ecommerce.kafka.product;

import com.romazal.ecommerce.product_movement.MovementType;

import java.util.UUID;

public record ProductMovementRecord(
        UUID productId,
        Double movementQuantity,
        Double stockQuantityBefore,
        Double stockQuantityAfter,
        MovementType movementType
) {
}
