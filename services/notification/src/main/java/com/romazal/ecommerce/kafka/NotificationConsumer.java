package com.romazal.ecommerce.kafka;

import com.romazal.ecommerce.email.EmailService;
import com.romazal.ecommerce.kafka.product.ProductThresholdNotification;
import com.romazal.ecommerce.notification.Notification;
import com.romazal.ecommerce.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.romazal.ecommerce.notification.NotificationType.PRODUCT_THRESHOLD_NOTIFICATION;

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



}


