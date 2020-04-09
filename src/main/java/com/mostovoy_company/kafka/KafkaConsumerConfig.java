package com.mostovoy_company.kafka;

import com.mostovoy_company.kafka.dto.ControlMessage;
import com.mostovoy_company.kafka.dto.RequestMessage;
import com.mostovoy_company.kafka.dto.ResponseMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.server}")
    private String kafkaServer;

    @Value("${kafka.group.id}")
    private String kafkaGroupId;

    /**
     * Consumer configuration for {@link RequestMessage}
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, RequestMessage> requestMessageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, RequestMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(requestMessageConsumerFactory());
        factory.setBatchListener(false);
        factory.setMessageConverter(new StringJsonMessageConverter());
        return factory;
    }

    @Bean
    public ConsumerFactory<Long, RequestMessage> requestMessageConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(requestMessageConsumerConfigs());
    }

    @Bean
    public Map<String, Object> requestMessageConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, Integer.MAX_VALUE);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        return props;
    }
    /*
     * End configuration
     */

    /**
     * Consumer configuration for {@link ResponseMessage}
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, ResponseMessage> responseMessageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, ResponseMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(responseMessageConsumerFactory());
        factory.setBatchListener(false);
        factory.setMessageConverter(new StringJsonMessageConverter());
        return factory;
    }

    @Bean
    public ConsumerFactory<Long, ResponseMessage> responseMessageConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(responseMessageProducerConfigs());
    }

    @Bean
    public Map<String, Object> responseMessageProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, Integer.MAX_VALUE);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        return props;
    }
    /*
     * End configuration
     */


    /**
     * Consumer configuration for {@link ControlMessage}
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, ControlMessage> controlMessageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, ControlMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(controlMessageConsumerFactory());
        factory.setBatchListener(false);
        factory.setMessageConverter(new StringJsonMessageConverter());
        return factory;
    }

    @Bean
    public ConsumerFactory<Long, ControlMessage> controlMessageConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(controlMessageProducerConfigs());
    }

    @Bean
    public Map<String, Object> controlMessageProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, Integer.MAX_VALUE);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        return props;
    }
    /*
     * End configuration
     */
    @Bean
    public KafkaListenerContainerFactory<?> kafkaListenerContainerFactory() {
        return new ConcurrentKafkaListenerContainerFactory<>();
    }

    @Bean
    public StringJsonMessageConverter converter() {
        return new StringJsonMessageConverter();
    }
}
