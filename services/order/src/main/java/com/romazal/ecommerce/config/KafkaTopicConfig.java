package com.romazal.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderConfirmationTopic() {
        return TopicBuilder
                .name("order-confirmation-topic")
                .build();
    }

    @Bean
    public NewTopic orderPaymentLinkTopic() {
        return TopicBuilder
                .name("order-payment-link-topic")
                .build();
    }

    @Bean
    public NewTopic orderCancellationTopic() {
        return TopicBuilder
                .name("order-cancellation-topic")
                .build();
    }
}
