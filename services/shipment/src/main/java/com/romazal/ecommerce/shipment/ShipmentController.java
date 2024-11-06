package com.romazal.ecommerce.shipment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PutMapping("/confirm")
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

    @PutMapping("/refund/order/{order-id}")
    public ResponseEntity<Void> refundShipmentByOrderId(@PathVariable("order-id") UUID orderId) {
        service.refundShipmentByOrderId(orderId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{shipment-id}")
    public ResponseEntity<ShipmentResponse> getShipmentByShipmentId(@PathVariable("shipment-id") UUID shipmentId) {
        return ResponseEntity.ok(service.getShipmentByShipmentId(shipmentId));
    }

    @GetMapping("/tracking/{tracking-number}")
    public ResponseEntity<ShipmentResponse> getShipmentByTrackingNumber(@PathVariable("tracking-number") String trackingNumber) {
        return ResponseEntity.ok(service.getShipmentByTrackingNumber(trackingNumber));
    }

    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getAllShipments() {
        return ResponseEntity.ok(service.getAllShipments());
    }
}
