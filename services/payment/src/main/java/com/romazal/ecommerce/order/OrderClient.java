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
    @PutMapping("/confirm/{order-id}")
    UUID confirmOrder(@PathVariable("order-id") UUID orderId);
}
