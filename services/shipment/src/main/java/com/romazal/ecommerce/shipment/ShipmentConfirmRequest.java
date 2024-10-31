package com.romazal.ecommerce.shipment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShipmentConfirmRequest(
        UUID shippingId,

        @NotBlank(message = "Tracking number cannot be blank")
        @Size(max = 20, message = "Tracking number must be 20 characters or less")
        String trackingNumber,

        @NotBlank(message = "Logistics provider cannot be blank")
        @Size(max = 50, message = "Logistics provider must be 50 characters or less")
        String logisticsProvider,

        @NotNull(message = "Estimated delivery date cannot be null")
        @Future(message = "Estimated delivery date must be in the future")
        LocalDateTime estimatedDeliveryDate
) {
}
