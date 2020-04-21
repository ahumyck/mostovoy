package com.mostovoy_company.services.kafka;

import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class ResponseService {

    private KafkaTemplate<Long, ResponseMessage> kafkaResponseTemplate;

    public ResponseService(KafkaTemplate<Long, ResponseMessage> kafkaResponseTemplate) {
        this.kafkaResponseTemplate = kafkaResponseTemplate;
    }

    public void sendResponseMessage(ResponseMessage message) {
        kafkaResponseTemplate.send("server.response", message);
    }
}
