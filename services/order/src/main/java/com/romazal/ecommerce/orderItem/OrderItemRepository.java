package com.romazal.ecommerce.orderItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    List<OrderItem> findAllByOrderId(UUID orderId);

    List<OrderItem> deleteAllByOrderId(UUID orderId);

}
