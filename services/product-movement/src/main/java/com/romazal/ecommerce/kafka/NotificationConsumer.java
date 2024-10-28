package com.romazal.ecommerce.kafka;

import com.romazal.ecommerce.kafka.product.ProductMovementRecord;
import com.romazal.ecommerce.product_movement.ProductMovement;
import com.romazal.ecommerce.product_movement.ProductMovementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final ProductMovementRepository repository;

    @KafkaListener(topics = "product-movement-topic")
    public void consumeProductMovementRecord(ProductMovementRecord productMovementRecord) throws MessagingException {
        log.info("Consuming the message from product-movement-topic Topic:: {}", productMovementRecord);

        repository.save(
                ProductMovement.builder()
                        .productId(productMovementRecord.productId())
                        .movementQuantity(productMovementRecord.movementQuantity())
                        .stockQuantityBefore(productMovementRecord.stockQuantityBefore())
                        .stockQuantityAfter(productMovementRecord.stockQuantityAfter())
                        .movementType(productMovementRecord.movementType())
                        .build()
        );

    }
}
