package com.romazal.ecommerce.product_movement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductMovementRepository extends JpaRepository<ProductMovement, UUID> {
    List<ProductMovement> findAllByProductIdOrderByCreatedDateDesc(UUID productId);
}
