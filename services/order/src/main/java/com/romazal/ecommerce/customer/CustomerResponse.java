package com.romazal.ecommerce.customer;

public record CustomerResponse(
        Long customerId,
        String username,
        String firstname,
        String lastname,
        String phoneNumber,
        String email,
        String shippingAddress,
        String billingAddress
) {
}
