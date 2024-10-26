package com.romazal.ecommerce.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "order-service",
        url = "${application.config.order-url}"
)
public interface OrderClient {
    @GetMapping("/customer/{customer-id}")
    List<OrderResponse> getAllOrdersByCustomerId(@PathVariable("customer-id") Long customerId);
}
