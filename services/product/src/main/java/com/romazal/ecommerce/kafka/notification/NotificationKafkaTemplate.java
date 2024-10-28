package com.romazal.ecommerce.kafka.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaTemplate  {
    private final KafkaTemplate<String, ProductThresholdNotification> kafkaTemplate;

    public void sendProductThresholdNotification(ProductThresholdNotification productThresholdNotification) {
        log.info("Sending product threshold notification to vendor: {}, over product: {}",
                productThresholdNotification.storeName(), productThresholdNotification.productId());

        Message<ProductThresholdNotification> message = MessageBuilder
                .withPayload(productThresholdNotification)
                .setHeader(TOPIC, "product-threshold-topic")
                .build();

        kafkaTemplate.send(message);
    }

}
