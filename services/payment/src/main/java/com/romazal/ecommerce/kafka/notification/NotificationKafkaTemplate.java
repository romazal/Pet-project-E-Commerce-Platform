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
    private final KafkaTemplate<String, PaymentConfirmationNotification> kafkaTemplateConfirmationNotification;
    private final KafkaTemplate<String, PaymentRefundNotification> kafkaTemplateRefundNotification;

    public void sendPaymentConfirmationNotification(PaymentConfirmationNotification paymentConfirmationNotification) {
        log.info("Sending payment confirmation notification to customer: {}, for order ID: {}",
                paymentConfirmationNotification.customerName(), paymentConfirmationNotification.orderId());

        Message<PaymentConfirmationNotification> message = MessageBuilder
                .withPayload(paymentConfirmationNotification)
                .setHeader(TOPIC, "payment-confirmation-topic")
                .build();

        kafkaTemplateConfirmationNotification.send(message);
    }

    public void sendPaymentRefundNotification(PaymentRefundNotification paymentRefundNotification) {
        log.info("Sending payment refund notification to customer: {}, for order ID: {}",
                paymentRefundNotification.customerName(), paymentRefundNotification.orderId());

        Message<PaymentRefundNotification> message = MessageBuilder
                .withPayload(paymentRefundNotification)
                .setHeader(TOPIC, "payment-refund-topic")
                .build();

        kafkaTemplateRefundNotification.send(message);
    }

}
