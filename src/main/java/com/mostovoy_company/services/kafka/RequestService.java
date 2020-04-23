package com.mostovoy_company.services.kafka;


import com.mostovoy_company.services.kafka.dto.RequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
//@Component
public class RequestService {

    private KafkaTemplate<Long, RequestMessage> kafkaRequestTemplate;

    @Autowired
    public RequestService(KafkaTemplate<Long, RequestMessage> kafkaRequestTemplate) {
        this.kafkaRequestTemplate = kafkaRequestTemplate;
    }


    public void sendRequestMessage(RequestMessage message) {
        kafkaRequestTemplate.send("server.request", message);
    }
}
