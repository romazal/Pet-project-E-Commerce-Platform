package com.romazal.ecommerce.payment;

import org.springframework.stereotype.Service;

import static com.romazal.ecommerce.payment.PaymentStatus.*;

@Service
public class PaymentMapper {

    public Payment toPayment(PaymentRequest paymentRequest) {
        if (paymentRequest == null) return null;

        return new Payment().builder()
                .orderId(paymentRequest.orderId())
                .totalAmount(paymentRequest.totalAmount())
                .customerEmail(paymentRequest.customerEmail())
                .customerName(paymentRequest.customerName())
                .paymentMethod(paymentRequest.paymentMethod())
                .paymentStatus(PENDING)
                .build();
    }

    public PaymentResponse toPaymentResponse(Payment payment) {
        if (payment == null) return null;

        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getTotalAmount(),
                payment.getCustomerEmail(),
                payment.getCustomerName(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getCreatedDate(),
                payment.getLastModifiedDate()
        );
    }
}
