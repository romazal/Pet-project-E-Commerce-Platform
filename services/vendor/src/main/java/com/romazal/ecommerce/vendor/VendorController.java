package com.romazal.ecommerce.vendor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService service;

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody VendorRequest vendorRequest) {
        return ResponseEntity.ok(service.register(vendorRequest));
    }

}
