package com.romazal.ecommerce.order;

import com.romazal.ecommerce.orderItem.OrderItemsStatus;
import com.romazal.ecommerce.payment.PaymentRequest;
import com.romazal.ecommerce.shipping.ShippingCreationRequest;
import org.springframework.stereotype.Service;

import static com.romazal.ecommerce.order.OrderStatus.UNFINISHED;
import static com.romazal.ecommerce.orderItem.OrderItemsStatus.UNRESERVED;

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
                .orderStatus(orderRequest.orderStatus() == null ? UNFINISHED : orderRequest.orderStatus())
                .orderItemsStatus(UNRESERVED)
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
                order.getCreatedDate(),
                order.getLastModifiedDate()
        );
    }

    public PaymentRequest toPaymentResponse(Order order) {
        if (order == null) return null;

        return new PaymentRequest(
                order.getOrderId(),
                order.getTotalAmount(),
                order.getPaymentMethod()
        );
    }

    public ShippingCreationRequest toShippingResponse(Order order) {
        if (order == null) return null;

        return new ShippingCreationRequest(
                order.getOrderId()
        );
    }
}
