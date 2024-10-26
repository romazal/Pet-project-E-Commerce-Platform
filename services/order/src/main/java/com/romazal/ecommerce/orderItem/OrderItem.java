package com.romazal.ecommerce.orderItem;

import com.romazal.ecommerce.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue
    private UUID orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;
}
