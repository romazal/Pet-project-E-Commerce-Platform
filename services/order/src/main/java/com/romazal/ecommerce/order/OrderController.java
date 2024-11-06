package com.romazal.ecommerce.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<UUID> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(service.createOrder(orderRequest));
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("order-id") UUID orderId) {
        return ResponseEntity.ok(service.getOrderById(orderId));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(service.getAllOrders());
    }

    @PutMapping
    public ResponseEntity<Void> updateOrder(@RequestBody OrderUpdateRequest orderUpdateRequest) {
        service.updateOrder(orderUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/queue-up/{order-id}")
    public ResponseEntity<UUID> queueUpOrder(@PathVariable("order-id") UUID orderId) {
        return ResponseEntity.ok(service.queueUpOrder(orderId));
    }

    @PutMapping("/confirm/{order-id}")
    public ResponseEntity<UUID> confirmOrder(@PathVariable("order-id") UUID orderId) {
        return ResponseEntity.ok(service.confirmOrder(orderId));
    }

    @PutMapping("/shipment/shipped/{order-id}")
    public ResponseEntity<UUID> setOrderStatusToShipped(@PathVariable("order-id") UUID orderId) {
        return ResponseEntity.ok(service.setOrderStatusToShipped(orderId));
    }

    @PutMapping("/shipment/delivered/{order-id}")
    public ResponseEntity<UUID> setOrderStatusToDelivered(@PathVariable("order-id") UUID orderId) {
        return ResponseEntity.ok(service.setOrderStatusToDelivered(orderId));
    }

    @PutMapping("/cancel/{order-id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable("order-id") UUID orderId) {
        service.cancelOrder(orderId);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("order-id") UUID orderId) {
        service.deleteOrder(orderId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/customer/{customer-id}")
    public ResponseEntity<List<OrderResponse>> getAllOrdersByCustomerId(@PathVariable("customer-id") Long customerId){
        return ResponseEntity.ok(service.getAllOrdersByCustomerId(customerId));
    }
}
