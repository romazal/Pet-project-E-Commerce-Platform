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
    private final KafkaTemplate<String, OrderPaymentLinkNotification> kafkaTemplatePaymentLinkNotification;
    private final KafkaTemplate<String, OrderConfirmationNotification> kafkaTemplateConfirmationNotification;
    private final KafkaTemplate<String, OrderCancellationNotification> kafkaTemplateCancellationNotification;

    public void sendOrderConfirmationNotification(OrderConfirmationNotification orderConfirmationNotification) {
        log.info("Sending order confirmation notification to customer: {}, for order ID: {}",
                orderConfirmationNotification.customerName(), orderConfirmationNotification.orderId());

        Message<OrderConfirmationNotification> message = MessageBuilder
                .withPayload(orderConfirmationNotification)
                .setHeader(TOPIC, "order-confirmation-topic")
                .build();

        kafkaTemplateConfirmationNotification.send(message);
    }

    public void sendOrderPaymentLinkNotification(OrderPaymentLinkNotification orderPaymentLinkNotification) {
        log.info("Sending order payment link notification to customer: {}, for order ID: {}",
                orderPaymentLinkNotification.customerName(), orderPaymentLinkNotification.orderId());

        Message<OrderPaymentLinkNotification> message = MessageBuilder
                .withPayload(orderPaymentLinkNotification)
                .setHeader(TOPIC, "order-payment-link-topic")
                .build();

        kafkaTemplatePaymentLinkNotification.send(message);
    }

    public void sendOrderCancellationNotification(OrderCancellationNotification orderCancellationNotification) {
        log.info("Sending order cancellation notification to customer: {}, for order ID: {}",
                orderCancellationNotification.customerName(), orderCancellationNotification.orderId());

        Message<OrderCancellationNotification> message = MessageBuilder
                .withPayload(orderCancellationNotification)
                .setHeader(TOPIC, "order-cancellation-topic")
                .build();

        kafkaTemplateCancellationNotification.send(message);
    }

}
