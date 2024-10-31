package com.romazal.ecommerce.shipment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService service;

    @PostMapping
    public ResponseEntity<UUID> createShipping(@RequestBody ShipmentCreationRequest shipmentCreationRequest) {
        return ResponseEntity.ok(service.createShipping(shipmentCreationRequest));
    }

    @PutMapping
    public ResponseEntity<UUID> confirmShipping(@RequestBody ShipmentConfirmRequest shipmentConfirmRequest) {
        return ResponseEntity.ok(service.confirmShipping(shipmentConfirmRequest));
    }

    @PutMapping("/success/{shipment-id}")
    public ResponseEntity<UUID> successShipping(@PathVariable("shipment-id") UUID shipmentId) {
        return ResponseEntity.ok(service.successShipping(shipmentId));
    }

    @PutMapping("/fail/{shipment-id}")
    public ResponseEntity<UUID> failShipping(@PathVariable("shipment-id") UUID shipmentId) {
        return ResponseEntity.ok(service.failShipping(shipmentId));
    }
}
