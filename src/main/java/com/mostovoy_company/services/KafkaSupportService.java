package com.mostovoy_company.services;

import com.mostovoy_company.chart.LineChartData;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.services.kafka.ControlService;
import com.mostovoy_company.services.kafka.ResponseService;
import com.mostovoy_company.services.kafka.dto.RequestMessage;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import com.mostovoy_company.services.kafka.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaSupportService extends BaseMainService {
    private ControlService controlService;
    private SessionManager sessionManager;
    private ResponseService responseService;

    public KafkaSupportService(List<LineChartData> charts,
                               ExperimentManager experimentManager,
                               ControlService controlService,
                               SessionManager sessionManager,
                               ResponseService responseService) {
        super(charts, experimentManager);
        this.controlService = controlService;
        this.sessionManager = sessionManager;
        this.responseService = responseService;
    }

    private void startNewSession() {
        controlService.sendStartMessage();
    }

    public void initNewSession() {
        sessionManager.initNewSession();
    }

    public void addMessage(RequestMessage message) {
        sessionManager.getCurrentSessionData().addMessage(message);
    }

    @Override
    protected RequestMessage buildRequestMessage(int count, int size, double probability) {
        return RequestMessage.builder()
                             .sessionId(sessionManager.getCurrentSessionId())
                             .nodeName(sessionManager.getNodeName())
                             .size(size)
                             .count(count)
                             .probability(probability)
                             .build();
    }

    @Override
    public void consume(ConsumeProperties consumeProperties) {
//        initNewSession();
        startNewSession();
    }

    @KafkaListener(topics = {"server.response"}, containerFactory = "responseMessageKafkaListenerContainerFactory")
    public void consumeResponseMessage(ResponseMessage message) {
        if (message.getSessionId() == sessionManager.getCurrentSessionId()) {
            addDotsToCharts(message);
        }
        log.info("=> consumed response  {}", message);
    }

    @KafkaListener(topics = "server.request",
            containerFactory = "requestMessageKafkaListenerContainerFactory",
            topicPartitions = @TopicPartition(topic = "server.request", partitions = SessionManager.NODE_NUMBER))
    public void consumeRequestMessage(final RequestMessage message) {
        log.info("=>start consumed request message {}", message);
        long startTime = System.currentTimeMillis();
        responseService.sendResponseMessage(performCalculation(message, new ConsumeProperties(true)));
        log.info("=> end consumed request message: " + (System.currentTimeMillis() - startTime));
        controlService.sendReadyMessage();
    }

    @Override
    protected ResponseMessage buildResponseMessage() {
        return ResponseMessage.builder().sessionId(sessionManager.getCurrentSessionId()).build();
    }
}