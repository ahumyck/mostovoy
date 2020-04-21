package com.mostovoy_company.services.kafka;


import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.expirement.entity.Statistic;
import com.mostovoy_company.expirement.filling.RandomFillingType;
import com.mostovoy_company.services.ConsumeProperties;
import com.mostovoy_company.services.kafka.dto.LineChartNode;
import com.mostovoy_company.services.kafka.dto.RequestMessage;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import com.mostovoy_company.services.kafka.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

@Slf4j
@Component
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
