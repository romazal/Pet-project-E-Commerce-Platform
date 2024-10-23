package com.romazal.ecommerce.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductsService service;

    @PostMapping
    public ResponseEntity<UUID> addProduct(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(service.addProduct(productRequest));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("product-id") UUID productId) {
        return ResponseEntity.ok(service.getProductById(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @PutMapping
    public ResponseEntity<Void> updateProduct(@RequestBody ProductRequest productRequest) {
        service.updateProduct(productRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("product-id") UUID productId) {
        service.deleteProduct(productId);
        return ResponseEntity.accepted().build();
    }


}
