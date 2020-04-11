package com.mostovoy_company.kafka;

import com.mostovoy_company.kafka.dto.ControlMessage;
import com.mostovoy_company.kafka.dto.RequestMessage;
import com.mostovoy_company.kafka.dto.ResponseMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.server}")
    private String kafkaServer;

    @Value("${kafka.producer.id}")
    private String kafkaProducerId;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerId);
        return props;
    }

    /**
     * Producer configuration for {@link ResponseMessage}
     */
    @Bean
    public ProducerFactory<Long, ResponseMessage> responseMessageProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, ResponseMessage> kafkaTemplateResponseMessage() {
        KafkaTemplate<Long, ResponseMessage> template = new KafkaTemplate<>(responseMessageProducerFactory());
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }

    /**
     * Producer configuration for {@link RequestMessage}
     */
    @Bean
    public ProducerFactory<Long, RequestMessage> requestMessageProducerFactory() {
        return new DefaultKafkaProducerFactory<>(requestProducerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, RequestMessage> kafkaTemplateRequestMessage() {
        KafkaTemplate<Long, RequestMessage> template = new KafkaTemplate<>(requestMessageProducerFactory());
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }

    @Bean
    public Map<String, Object> requestProducerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerId);
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RequestPartitioner.class.getCanonicalName());
        props.put("partitions.0", "node0");
        props.put("partitions.1", "node1");
        props.put("partitions.2", "node2");
        props.put("partitions.3", "node3");
        props.put("partitions.4", "node4");
        props.put("partitions.5", "node5");
        props.put("partitions.6", "node6");
        return props;
    }

    /**
     * Producer configuration for {@link ControlMessage}
     */
    @Bean
    public ProducerFactory<Long, ControlMessage> controlMessageProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, ControlMessage> kafkaTemplateControlMessage() {
        KafkaTemplate<Long, ControlMessage> template = new KafkaTemplate<>(controlMessageProducerFactory());
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }
}