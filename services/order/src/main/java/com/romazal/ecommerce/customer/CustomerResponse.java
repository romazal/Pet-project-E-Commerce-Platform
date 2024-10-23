package com.romazal.ecommerce.customer;

public record CustomerResponse(
        String customerId,
        String firstname,
        String lastname,
        String email
) {
}
