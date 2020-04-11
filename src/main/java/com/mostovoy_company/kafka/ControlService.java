package com.mostovoy_company.kafka;

import com.mostovoy_company.chart.ChartsDataRepository;
import com.mostovoy_company.kafka.dto.ControlMessage;
import com.mostovoy_company.kafka.dto.RequestMessage;
import com.mostovoy_company.kafka.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ControlService {

    private SessionManager sessionManager;
    private KafkaTemplate<Long, ControlMessage> kafkaControlTemplate;
    private ChartsDataRepository chartsDataRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    public ControlService(SessionManager sessionManager,
                          KafkaTemplate<Long, ControlMessage> kafkaControlTemplate,
                          ChartsDataRepository chartsDataRepository) {
        this.sessionManager = sessionManager;
        this.kafkaControlTemplate = kafkaControlTemplate;
        this.chartsDataRepository = chartsDataRepository;
    }

    public void sendReadyMessage() {
        ControlMessage controlMessage = new ControlMessage(sessionManager.getCurrentSessionId(), sessionManager.getNodeName(), SessionManager.READY_ACTION);
        log.info("=> sends ready message: " + controlMessage);
        kafkaControlTemplate.send("server.control", controlMessage);
    }

    public void sendStartMessage() {
        ControlMessage controlMessage = new ControlMessage(sessionManager.getCurrentSessionId(), SessionManager.NODE_NAME, SessionManager.START_SESSION_ACTION);
        log.info("=> sends start session message: " + controlMessage);
        kafkaControlTemplate.send("server.control", controlMessage);
    }

    @KafkaListener(topics = {"server.control"}, containerFactory = "controlMessageKafkaListenerContainerFactory")
    public void consumeControlMessage(ControlMessage message) {
        if (message.getAction().equals(SessionManager.START_SESSION_ACTION)) {
            consumeStartSessionMessage(message);
        } else if (message.getAction().equals(SessionManager.READY_ACTION)) {
            consumeReadyMessage(message);
        } else if (message.getAction().equals(SessionManager.END_SESSION_ACTION)) {
            sessionManager.closeSession();
        }
    }

    private void consumeStartSessionMessage(ControlMessage message) {
        if (sessionManager.getCurrentSessionId() != message.getSessionId()) {
            sessionManager.updateSession(message.getSessionId());
            chartsDataRepository.clear();
        }
        sendReadyMessage();
    }

    private void consumeReadyMessage(ControlMessage message) {
        if (message.getSessionId() == sessionManager.getCurrentSessionId() && sessionManager.getCurrentSessionData().isMaster()) {
            RequestMessage response = sessionManager.getCurrentSessionData().nextMessage();
            log.info("=> consumed ready control message {}", message);
            if (response == null) {
                kafkaControlTemplate.send("server.control", new ControlMessage(sessionManager.getCurrentSessionId(), sessionManager.getNodeName(), SessionManager.END_SESSION_ACTION));
            } else {
                response.setNodeName(message.getNodeName());
                requestService.sendRequestMessage(response);
            }
        }
    }

}