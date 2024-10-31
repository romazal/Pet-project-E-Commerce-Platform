package com.romazal.ecommerce.shipment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "shipment-service",
        url = "${application.config.shipment-url}"
)
public interface ShipmentClient {
    @PostMapping
    UUID createShipping(@RequestBody ShipmentCreationRequest shipmentCreationRequest);
}
