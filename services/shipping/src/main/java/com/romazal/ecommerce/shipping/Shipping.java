package com.romazal.ecommerce.shipping;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.romazal.ecommerce.shipping.DeliveryStatus.PENDING;
import static jakarta.persistence.EnumType.STRING;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "shippings")
public class Shipping {

    @Id
    @GeneratedValue
    private UUID shippingId;

    @Column(nullable = false, unique = true)
    private UUID orderId;

    @Column(nullable = false)
    private String trackingNumber = "Default TrackingNumber";

    @Column(nullable = false)
    private String logisticsProvider = "Default LogisticsProvider";

    @Enumerated(STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus = PENDING;

    private LocalDateTime estimatedDeliveryDate;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

}
