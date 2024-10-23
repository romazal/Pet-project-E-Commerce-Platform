package com.romazal.ecommerce.order;

import com.romazal.ecommerce.orderItem.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        Long customerId,
        BigDecimal totalAmount,
        PaymentStatus paymentStatus,
        OrderStatus orderStatus,
        PaymentMethod paymentMethod,
        String shippingAddress,
        List<OrderItem> orderItems,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
