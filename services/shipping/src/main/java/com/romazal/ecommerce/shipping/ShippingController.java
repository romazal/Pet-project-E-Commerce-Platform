package com.romazal.ecommerce.shipping;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shippings")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService service;

    @PostMapping
    public ResponseEntity<UUID> createShipping(@RequestBody ShippingCreationRequest shippingCreationRequest) {
        return ResponseEntity.ok(service.createShipping(shippingCreationRequest));
    }

    @PutMapping
    public ResponseEntity<UUID> confirmShipping(@RequestBody ShippingConfirmRequest shippingConfirmRequest) {
        return ResponseEntity.ok(service.confirmShipping(shippingConfirmRequest));
    }

    @PutMapping("/success/{shipping-id}")
    public ResponseEntity<UUID> successShipping(@PathVariable("shipping-id") UUID shippingId) {
        return ResponseEntity.ok(service.successShipping(shippingId));
    }

    @PutMapping("/fail/{shipping-id}")
    public ResponseEntity<UUID> failShipping(@PathVariable("shipping-id") UUID shippingId) {
        return ResponseEntity.ok(service.failShipping(shippingId));
    }
}
