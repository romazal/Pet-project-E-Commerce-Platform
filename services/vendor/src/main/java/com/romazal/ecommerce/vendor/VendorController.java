package com.romazal.ecommerce.vendor;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService service;

    @PostMapping("/register")
    public ResponseEntity<Long> registerVendor(@RequestBody @Valid VendorRequest vendorRequest) {
        return ResponseEntity.ok(service.registerVendor(vendorRequest));
    }


    @GetMapping("/profiles/{vendor-id}")
    public ResponseEntity<VendorResponse> getVendorById(@PathVariable("vendor-id") Long vendorId) {
        return ResponseEntity.ok(service.getVendorById(vendorId));
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<VendorResponse>> getAllVendors() {
        return ResponseEntity.ok(service.getAllVendors());
    }

    @GetMapping("/exists/{vendor-id}")
    public ResponseEntity<Boolean> existsById(@PathVariable("vendor-id") Long vendorId) {
        return ResponseEntity.ok(service.existsById(vendorId));
    }

    @PutMapping("/profiles")
    public ResponseEntity<Void> updateVendorById(
            @RequestBody @Valid VendorRequest vendorRequest
    ) {
        service.updateVendorById(vendorRequest);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/profiles/{vendor-id}")
    public ResponseEntity<Void> deleteVendorById(
            @PathVariable("vendor-id") Long vendorId
    ) {
        service.deleteVendorById(vendorId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/products/{vendor-id}")
    public ResponseEntity<List> getAllProductsByVendorId(@PathVariable("vendor-id") Long vendorId) {
        return ResponseEntity.ok(service.getAllProductsByVendorId(vendorId));
    }

    @GetMapping("/contacts/{vendor-id}")
    public ResponseEntity<VendorContactResponse> getVendorContactsByVendorId(@PathVariable("vendor-id") Long vendorId){
        return ResponseEntity.ok(service.getVendorContactsByVendorId(vendorId));
    }


}
