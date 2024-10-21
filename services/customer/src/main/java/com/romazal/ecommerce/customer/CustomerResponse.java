package com.romazal.ecommerce.customer;

public record CustomerResponse(
        String username,
        String firstname,
        String lastname,
        String phoneNumber,
        String email,
        String shippingAddress,
        String billingAddress
) {

}
