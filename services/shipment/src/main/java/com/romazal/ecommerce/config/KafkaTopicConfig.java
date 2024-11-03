package com.romazal.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic shipmentShippedTopic() {
        return TopicBuilder
                .name("shipment-shipped-topic")
                .build();
    }

    @Bean
    public NewTopic shipmentDeliveredTopic() {
        return TopicBuilder
                .name("shipment-delivered-topic")
                .build();
    }


}
