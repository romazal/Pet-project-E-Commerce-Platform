package com.romazal.ecommerce.productMovement;

import org.springframework.stereotype.Service;

@Service
public class ProductMovementMapper {
    public ProductMovementResponse toProductMovementResponse(ProductMovement productMovement) {
        if (productMovement == null) return null;

        return new ProductMovementResponse(
                productMovement.getProductMovementId(),
                productMovement.getProductId(),
                productMovement.getQuantity(),
                productMovement.getMovementType(),
                productMovement.getCreatedDate()
        );
    }
}
