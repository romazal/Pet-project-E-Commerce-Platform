package com.romazal.ecommerce.kafka;

import com.romazal.ecommerce.email.EmailService;
import com.romazal.ecommerce.kafka.product.ProductThresholdNotification;
import com.romazal.ecommerce.kafka.shipment.ShipmentShippedNotification;
import com.romazal.ecommerce.notification.Notification;
import com.romazal.ecommerce.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.romazal.ecommerce.notification.NotificationType.PRODUCT_THRESHOLD_NOTIFICATION;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationRepository repository;
    private final EmailService emailService;

    @KafkaListener(topics = "product-threshold-topic")
    public void consumeProductThresholdNotification(ProductThresholdNotification productThresholdNotification) throws MessagingException {
        log.info("Consuming the message from product-threshold-topic Topic:: {}", productThresholdNotification);

        repository.save(
                Notification.builder()
                        .type(PRODUCT_THRESHOLD_NOTIFICATION)
                        .destinationEmail(productThresholdNotification.storeEmail())
                        .message(
                                format(
                                        """
                                        Product Threshold Notification:
                                        
                                        The current stock quantity for the product '%s' has fallen below the specified threshold value.
                                        
                                        Current Stock Quantity: %.2f
                                        Threshold Quantity: %.2f
                                        """,
                                        productThresholdNotification.productName(),
                                        productThresholdNotification.stockQuantity(),
                                        productThresholdNotification.thresholdQuantity()
                                )
                        )
                        .notificationDate(LocalDateTime.now())
                        .productThresholdNotification(productThresholdNotification)
                        .build()
        );

        emailService.sendProductThresholdNotificationEmail(
                productThresholdNotification.storeEmail(),
                productThresholdNotification.storeName(),
                productThresholdNotification.productName(),
                productThresholdNotification.productId(),
                productThresholdNotification.sku(),
                productThresholdNotification.stockQuantity(),
                productThresholdNotification.thresholdQuantity()
        );
    }

    @KafkaListener(topics = "shipment-shipped-topic")
    public void consumeShipmentShippedNotification(ShipmentShippedNotification shipmentShippedNotification) throws MessagingException {
        log.info("Consuming the message from shipment-shipped-topic Topic:: {}", shipmentShippedNotification);

        repository.save(
                Notification.builder()
                        .type(PRODUCT_THRESHOLD_NOTIFICATION)
                        .destinationEmail(shipmentShippedNotification.customerEmail())
                        .message(
                                format(
                                        """
                                        Shipment Shipped Notification:
                                        
                                        A shipment for order ID: %s has been initiated.
                                        
                                        Shipped Date: %s
                                        Estimated Delivery Date: %s
                                        """,
                                        shipmentShippedNotification.orderId(),
                                        shipmentShippedNotification.shippedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                        shipmentShippedNotification.estimatedDeliveryDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                )
                        )
                        .notificationDate(LocalDateTime.now())
                        .shipmentShippedNotification(shipmentShippedNotification)
                        .build()
        );

        emailService.sendShipmentShippedNotificationEmail(
                shipmentShippedNotification.customerEmail(),
                shipmentShippedNotification.shipmentId(),
                shipmentShippedNotification.orderId(),
                shipmentShippedNotification.customerName(),
                shipmentShippedNotification.trackingNumber(),
                shipmentShippedNotification.logisticsProvider(),
                shipmentShippedNotification.shippedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                shipmentShippedNotification.estimatedDeliveryDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }



}


