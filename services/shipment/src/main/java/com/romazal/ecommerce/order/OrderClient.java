package com.romazal.ecommerce.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(
        name = "order-service",
        url = "${application.config.order-url}"
)
public interface OrderClient {
    @PutMapping("/shipment/shipped/{order-id}")
    UUID setOrderStatusToShipped(@PathVariable("order-id") UUID orderId);

    @PutMapping("/shipment/delivered/{order-id}")
    UUID setOrderStatusToDelivered(@PathVariable("order-id") UUID orderId);
}
