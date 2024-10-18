package com.romazal.ecommerce.vendor;

import jakarta.validation.constraints.*;

public record VendorRequest(
        Long id,

        @NotNull(message = "Username is required")
        @NotEmpty(message = "Username cannot be empty")
        @Size(min = 3, max = 50,
                message = "Username must be between 3 and 50 characters long")
        String username,

        @NotNull(message = "Password is required")
        @NotEmpty(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
        String password,

        @NotNull(message = "Store name is required")
        @NotEmpty(message = "Store name cannot be empty")
        @Size(max = 100,
                message = "Store name cannot exceed 100 characters")
        String storeName,

        @NotNull(message = "Business license number is required")
        @NotEmpty(message = "Business license number cannot be empty")
        @Size(max = 50,
                message = "Business license number cannot exceed 50 characters")
        String businessLicenseNumber,

        @NotNull(message = "Store address is required")
        @NotEmpty(message = "Store address cannot be empty")
        @Size(min = 5, max = 255,
                message = "Store address must be between 5 and 255 characters long")
        String storeAddress,

        @NotNull(message = "Store phone is required")
        @NotEmpty(message = "Store phone cannot be empty")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$",
                message = "Store phone must be between 10 and 15 digits, with optional leading '+'")
        String storePhone,

        @NotNull(message = "Email is required")
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email must be valid")
        String storeEmail,

        @Size(max = 500,
                message = "Description cannot exceed 500 characters")
        String description

) {
}
