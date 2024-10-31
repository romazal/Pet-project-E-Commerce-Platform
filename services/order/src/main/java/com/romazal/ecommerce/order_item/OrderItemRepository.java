package com.romazal.ecommerce.order_item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    List<OrderItem> findAllByOrder_OrderId(UUID orderId);

    List<OrderItem> deleteAllByOrder_OrderId(UUID orderId);

}
