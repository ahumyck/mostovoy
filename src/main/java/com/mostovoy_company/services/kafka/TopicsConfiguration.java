package com.mostovoy_company.services.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

//@Configuration
public class TopicsConfiguration {
    @Bean
    public NewTopic responseTopic() {
        return TopicBuilder.name("server.response")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic requestTopic() {
        return TopicBuilder.name("server.request")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic controlTopic() {
        return TopicBuilder.name("server.control")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
