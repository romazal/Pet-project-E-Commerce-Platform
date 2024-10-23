package com.romazal.ecommerce.order;

import org.springframework.stereotype.Service;

@Service
public class OrderMapper {
    public Order toOrder(OrderRequest orderRequest) {
        if (orderRequest == null) return null;

        return new Order().builder()
                .orderId(orderRequest.orderId())
                .customerId(orderRequest.customerId())
                .totalAmount(orderRequest.totalAmount())
                .paymentStatus(orderRequest.paymentStatus())
                .orderStatus(orderRequest.orderStatus())
                .paymentMethod(orderRequest.paymentMethod())
                .shippingAddress(orderRequest.shippingAddress())
                .orderItems(orderRequest.orderItems())
                .build();

    }

    public OrderResponse toOrderResponse(Order order) {
        if (order == null) return null;

        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getPaymentStatus(),
                order.getOrderStatus(),
                order.getPaymentMethod(),
                order.getShippingAddress(),
                order.getOrderItems(),
                order.getCreatedDate(),
                order.getLastModifiedDate()
        );
    }
}
