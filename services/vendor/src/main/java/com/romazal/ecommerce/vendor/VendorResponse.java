package com.romazal.ecommerce.vendor;

public record VendorResponse(
        String username,
        String storeName,
        String storeAddress,
        String storePhone,
        String storeEmail,
        String description,
        Status status
) {
}
