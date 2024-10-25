package com.romazal.ecommerce.payment;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.romazal.ecommerce.payment.PaymentStatus.*;
import static jakarta.persistence.EnumType.STRING;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue
    private UUID paymentId;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PENDING;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

}
