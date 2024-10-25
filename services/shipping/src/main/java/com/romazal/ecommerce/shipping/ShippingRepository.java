package com.romazal.ecommerce.shipping;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShippingRepository extends JpaRepository<Shipping, UUID> {
}
