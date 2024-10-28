package com.romazal.ecommerce.product_movement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-movements")
@RequiredArgsConstructor
public class ProductMovementController {

    private final ProductMovementService service;

    @GetMapping
    public ResponseEntity<List<ProductMovementResponse>> getAllProductMovements() {
        return ResponseEntity.ok(service.getAllProductMovements());
    }

    @GetMapping("/{product-movement-id}")
    public ResponseEntity<ProductMovementResponse> getProductMovementById(@PathVariable("product-movement-id") UUID productMovementId) {
        return ResponseEntity.ok(service.getProductMovementById(productMovementId));
    }

    @GetMapping("/product/{product-id}")
    public ResponseEntity<List<ProductMovementResponse>> getAllProductMovementsByProductId(@PathVariable("product-id") UUID productId) {
        return ResponseEntity.ok(service.getAllProductMovementsByProductId(productId));
    }


}
