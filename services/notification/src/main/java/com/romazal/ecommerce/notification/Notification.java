package com.romazal.ecommerce.notification;

import com.romazal.ecommerce.kafka.product.ProductThresholdNotification;
import com.romazal.ecommerce.kafka.shipment.ShipmentShippedNotification;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Notification {
    @Id
    private String id;

    private String destinationEmail;

    private String message;

    private NotificationType type;

    private LocalDateTime notificationDate;

    private ProductThresholdNotification productThresholdNotification;

    private ShipmentShippedNotification shipmentShippedNotification;
}
