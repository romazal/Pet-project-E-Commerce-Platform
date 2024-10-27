package com.romazal.ecommerce.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE Payment p SET p.paymentStatus = 'FAILED' WHERE p.orderId = :orderId AND p.paymentStatus = 'PENDING'")
    int updatePendingPaymentStatusToFailedByOrderId (@Param("orderId") UUID orderId);

    Optional<Payment> findFirstByOrderIdOrderByCreatedDateDesc(UUID orderId);

    List<Payment> findAllByOrderIdOrderByCreatedDateDesc(UUID orderId);
}
