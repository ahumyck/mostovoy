package com.mostovoy_company.services;

import com.mostovoy_company.chart.LineChartData;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.services.kafka.ControlService;
import com.mostovoy_company.services.kafka.dto.RequestMessage;
import com.mostovoy_company.services.kafka.session.SessionManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

//@Service
@Slf4j
public class KafkaSupportService extends BaseMainService {
    private ControlService controlService;
    private SessionManager sessionManager;

    public KafkaSupportService(List<LineChartData> charts,
                               ExperimentManager experimentManager,
                               ControlService controlService,
                               SessionManager sessionManager) {
        super(charts, experimentManager);
        this.controlService = controlService;
        this.sessionManager = sessionManager;
    }

    private void startNewSession() {
        controlService.sendStartMessage();
    }

    private void initNewSession() {
        sessionManager.initNewSession();
    }

    public void add(int count, int size, double probability) {
        sessionManager.getCurrentSessionData().addMessage(count, size, probability);
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
        initNewSession();
        startNewSession();
    }
}