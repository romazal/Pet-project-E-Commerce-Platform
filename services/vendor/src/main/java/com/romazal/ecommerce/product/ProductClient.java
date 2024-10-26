package com.romazal.ecommerce.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "product-service",
        url = "${application.config.product-url}"
)
public interface ProductClient {
    @GetMapping("/vendor/{vendor-id}")
    List<ProductResponse> getAllProductsByVendorId(@PathVariable("vendor-id") Long vendorId);
}
