package com.romazal.ecommerce.payment;

import org.springframework.stereotype.Service;

@Service
public class PaymentMapper {

    public Payment toPayment(PaymentRequest paymentRequest) {
        if (paymentRequest == null) return null;

        return new Payment().builder()
                .orderId(paymentRequest.orderId())
                .totalAmount(paymentRequest.totalAmount())
                .paymentMethod(paymentRequest.paymentMethod())
                .build();
    }

    public PaymentResponse toPaymentResponse(Payment payment) {
        if (payment == null) return null;

        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getTotalAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getCreatedDate(),
                payment.getLastModifiedDate()
        );
    }
}
