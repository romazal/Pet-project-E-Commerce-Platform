package com.romazal.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProductThresholdTopicConfig {

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder
                .name("product-threshold-topic")
                .build();
    }
}
