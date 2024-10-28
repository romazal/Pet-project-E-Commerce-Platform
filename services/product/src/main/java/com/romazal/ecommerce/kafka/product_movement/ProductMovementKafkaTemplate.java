package com.romazal.ecommerce.kafka.product_movement;

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
public class ProductMovementKafkaTemplate {
    private final KafkaTemplate<String, ProductMovementRecord> kafkaTemplate;

    public void sendProductMovementRecord(ProductMovementRecord productMovementRecord) {
        log.info("Sending product movement record to Product Movement service. Product ID:: {}, movement type:: {}",
                productMovementRecord.productId(), productMovementRecord.movementType());

        Message<ProductMovementRecord> message = MessageBuilder
                .withPayload(productMovementRecord)
                .setHeader(TOPIC, "product-movement-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
