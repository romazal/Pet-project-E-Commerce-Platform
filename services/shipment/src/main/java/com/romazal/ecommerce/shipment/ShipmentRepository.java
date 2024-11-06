package com.romazal.ecommerce.shipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {
    Optional<Shipment> findFirstByOrderIdAndDeliveryStatusNot(UUID orderId, DeliveryStatus deliveryStatus);

    Optional<Shipment> findByTrackingNumber(String trackingNumber);
}
