package com.romazal.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic productThresholdTopic() {
        return TopicBuilder
                .name("product-threshold-topic")
                .build();
    }

    @Bean
    public NewTopic productMovementTopic() {
        return TopicBuilder
                .name("product-movement-topic")
                .build();
    }
}
