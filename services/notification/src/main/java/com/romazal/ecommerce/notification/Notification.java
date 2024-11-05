package com.romazal.ecommerce.notification;

import com.romazal.ecommerce.kafka.order.OrderCancellationNotification;
import com.romazal.ecommerce.kafka.order.OrderConfirmationNotification;
import com.romazal.ecommerce.kafka.order.OrderPaymentLinkNotification;
import com.romazal.ecommerce.kafka.payment.PaymentConfirmationNotification;
import com.romazal.ecommerce.kafka.payment.PaymentRefundNotification;
import com.romazal.ecommerce.kafka.product.ProductThresholdNotification;
import com.romazal.ecommerce.kafka.shipment.ShipmentDeliveredNotification;
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

    private OrderPaymentLinkNotification orderPaymentLinkNotification;

    private OrderConfirmationNotification orderConfirmationNotification;

    private OrderCancellationNotification orderCancellationNotification;

    private PaymentConfirmationNotification paymentConfirmationNotification;

    private PaymentRefundNotification paymentRefundNotification;

    private ShipmentShippedNotification shipmentShippedNotification;

    private ShipmentDeliveredNotification shipmentDeliveredNotification;


}
