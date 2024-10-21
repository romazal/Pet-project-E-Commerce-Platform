package com.romazal.ecommerce.customer;

import jakarta.validation.constraints.*;

public record CustomerRequest(
        Long id,

        @NotNull(message = "Username is required")
        @NotEmpty(message = "Username cannot be empty")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters long")
        String username,

        @NotNull(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
        String password,

        @NotNull(message = "First name is required")
        @NotEmpty(message = "First name cannot be empty")
        @Pattern(regexp = "^[a-zA-Z\\s'-]+$",
                message = "First name can only contain letters, spaces, hyphens, and apostrophes")
        String firstname,

        @NotNull(message = "Last name is required")
        @NotEmpty(message = "Last name cannot be empty")
        @Pattern(regexp = "^[a-zA-Z\\s'-]+$",
                message = "Last name can only contain letters, spaces, hyphens, and apostrophes")
        String lastname,

        @NotNull(message = "Phone number is required")
        @NotEmpty(message = "Phone number cannot be empty")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$",
                message = "Phone number must be between 10 and 15 digits, with optional leading '+'")
        String phoneNumber,

        @NotNull(message = "Email is required")
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email must be valid")
        String email,

        @NotNull(message = "Shipping address is required")
        @NotEmpty(message = "Shipping address cannot be empty")
        @Size(min = 5, max = 255,
                message = "Shipping address must be between 5 and 255 characters long")
        String shippingAddress,

        @NotNull(message = "Billing address is required")
        @NotEmpty(message = "Billing address cannot be empty")
        @Size(min = 5, max = 255,
                message = "Billing address must be between 5 and 255 characters long")
        String billingAddress

) {

}
