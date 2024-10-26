package com.romazal.ecommerce.productMovement;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "product_movements")
public class ProductMovement {

    @Id
    @GeneratedValue
    private UUID productMovementId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private MovementType movementType;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

}
