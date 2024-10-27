package com.romazal.ecommerce.vendor;

public record VendorContactResponse(
        String storeName,
        String storeAddress,
        String storePhone,
        String storeEmail
) {
}
