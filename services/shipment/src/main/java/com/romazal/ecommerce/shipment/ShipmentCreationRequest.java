package com.romazal.ecommerce.shipment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ShipmentCreationRequest(
        @NotNull(message = "Order ID is required")
        @Positive(message = "Order ID must be positive")
        UUID orderId,

        @NotNull(message = "Customer Email is required")
        String customerEmail,

        @NotNull(message = "Customer Name is required")
        String customerName
) {
}
