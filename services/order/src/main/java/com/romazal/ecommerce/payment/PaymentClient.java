package com.romazal.ecommerce.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "payment-service",
        url = "${application.config.payment-url}"
)
public interface PaymentClient {

    @PostMapping("/create")
    UUID createPayment(@RequestBody PaymentRequest request);

    @PostMapping("/fail/order/{order-id}")
    void failPaymentByOrderId(@PathVariable("order-id") UUID orderId);

    @PostMapping("/refund/order/{order-id}")
    void refundPaymentByOrderId(@PathVariable("order-id") UUID orderId);

}
