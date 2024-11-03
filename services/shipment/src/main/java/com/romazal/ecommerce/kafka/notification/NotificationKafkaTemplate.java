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
public class NotificationKafkaTemplate {
    private final KafkaTemplate<String, ShipmentShippedNotification> kafkaTemplateShippedNotification;
    private final KafkaTemplate<String, ShipmentDeliveredNotification> kafkaTemplateDeliveredNotification;

    public void sendShipmentShippedNotification(ShipmentShippedNotification shipmentShippedNotification) {
        log.info("Sending shipment has been shipped notification to customer email:: {}, with shipment ID:: {}, with order ID:: {}",
                shipmentShippedNotification.customerEmail(), shipmentShippedNotification.shipmentId(), shipmentShippedNotification.orderId());

        Message<ShipmentShippedNotification> message = MessageBuilder
                .withPayload(shipmentShippedNotification)
                .setHeader(TOPIC, "shipment-shipped-topic")
                .build();

        kafkaTemplateShippedNotification.send(message);
    }

    public void sendShipmentDeliveredNotification(ShipmentDeliveredNotification shipmentDeliveredNotification) {
        log.info("Sending shipment has been delivered notification to customer:: {}, with shipment ID:: {}, with order ID:: {}",
                shipmentDeliveredNotification.customerName(), shipmentDeliveredNotification.shipmentId(), shipmentDeliveredNotification.orderId());

        Message<ShipmentDeliveredNotification> message = MessageBuilder
                .withPayload(shipmentDeliveredNotification)
                .setHeader(TOPIC, "shipment-delivered-topic")
                .build();

        kafkaTemplateDeliveredNotification.send(message);
    }

}
