package com.romazal.ecommerce.order;

import com.romazal.ecommerce.order_item.OrderItem;
import com.romazal.ecommerce.order_item.OrderItemsStatus;
import com.romazal.ecommerce.payment.PaymentMethod;
import com.romazal.ecommerce.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private UUID orderId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(STRING)
    @Column
    private PaymentStatus paymentStatus;

    @Enumerated(STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Enumerated(STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(STRING)
    @Column(nullable = false)
    private OrderItemsStatus orderItemsStatus;

    @Column(nullable = false)
    private String shippingAddress;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

}
