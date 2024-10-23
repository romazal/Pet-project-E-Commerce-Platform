package com.romazal.ecommerce.orderItem;

import com.romazal.ecommerce.order.Order;
import com.romazal.ecommerce.product.PurchaseRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderItemMapper {

    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        if (orderItem == null) return null;

        return new OrderItemResponse(
                orderItem.getOrderItemId(),
                orderItem.getOrder().getOrderId(),
                orderItem.getProductId(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice()
        );
    }

    public OrderItem toOrderItem(OrderItemRequest orderItemRequest) {
        if (orderItemRequest == null) return null;

        return new OrderItem().builder()
                .orderItemId(orderItemRequest.orderItemId())
                .order(
                        new Order().builder()
                                .orderId(orderItemRequest.orderId())
                                .build()
                )
                .productId(orderItemRequest.productId())
                .quantity(orderItemRequest.quantity())
                .unitPrice(orderItemRequest.unitPrice())
                .build();
    }

    public PurchaseRequest toPurchaseRequest(OrderItemResponse orderItemResponse) {
        if (orderItemResponse == null) return null;

        return new PurchaseRequest(
                orderItemResponse.productId(),
                orderItemResponse.quantity(),
                orderItemResponse.unitPrice()
        );
    }
}
