package com.romazal.ecommerce.kafka.order;


import com.romazal.ecommerce.kafka.product.PurchaseRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderPaymentLinkNotification(
        UUID orderId,
        UUID paymentId,
        String customerEmail,
        String customerName,
        BigDecimal totalAmount,
        List<PurchaseRequest> orderItems
) {
}
