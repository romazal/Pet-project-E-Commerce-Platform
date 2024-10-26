package com.romazal.ecommerce.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllProductsByVendorId(Long vendorId);

    List<Product> findAllByProductIdInOrderByProductId(List<UUID> productIds);
}
