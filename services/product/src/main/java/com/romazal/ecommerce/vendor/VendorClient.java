package com.romazal.ecommerce.vendor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "vendor-service",
        url = "${application.config.vendor-url}"
)
public interface VendorClient {
    @GetMapping("/contacts/{vendor-id}")
    VendorContactResponse getVendorContactsByVendorId(@PathVariable("vendor-id") Long vendorId);

    @GetMapping("/exists/{vendor-id}")
    Boolean existsById(@PathVariable("vendor-id") Long vendorId);
}
