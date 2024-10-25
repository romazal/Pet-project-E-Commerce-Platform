package com.romazal.ecommerce.shipping;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "shipping-service",
        url = "${application.config.shipping-url}"
)
public interface ShippingClient {
    @PostMapping
    UUID createShipping(@RequestBody ShippingCreationRequest shippingCreationRequest);
}
