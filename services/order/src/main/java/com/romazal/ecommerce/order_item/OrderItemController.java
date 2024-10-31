package com.romazal.ecommerce.order_item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService service;

    @PostMapping
    public ResponseEntity<UUID> saveOrderItem(@RequestBody OrderItemRequest orderItemRequest) {
        return ResponseEntity.ok(service.saveOrderItem(orderItemRequest));
    }

    @GetMapping("/order/{order-id}")
    private ResponseEntity<List<OrderItemResponse>> getAllOrderItemsByOrderId(@PathVariable("order-id") UUID orderId) {
        return ResponseEntity.ok(service.getAllOrderItemsByOrderId(orderId));
    }

    @DeleteMapping("/order/{order-id}")
    private ResponseEntity<Void> deleteAllOrderItemsByOrderId(@PathVariable("order-id") UUID orderId) {
        service.deleteAllOrderItemsByOrderId(orderId);
        return ResponseEntity.accepted().build();
    }

}
