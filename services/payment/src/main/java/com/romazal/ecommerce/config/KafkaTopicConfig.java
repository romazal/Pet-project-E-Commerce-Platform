package com.romazal.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic paymentConfirmationTopic() {
        return TopicBuilder
                .name("payment-confirmation-topic")
                .build();
    }

    @Bean
    public NewTopic paymentRefundTopic() {
        return TopicBuilder
                .name("payment-refund-topic")
                .build();
    }
}
