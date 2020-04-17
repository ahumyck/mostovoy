package com.mostovoy_company.services.kafka;

import com.mostovoy_company.services.BaseMainService;
import com.mostovoy_company.chart.ChartsDataRepository;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.services.ConsumeProperties;
import com.mostovoy_company.services.kafka.dto.RequestMessage;
import com.mostovoy_company.services.kafka.session.SessionManager;
import com.mostovoy_company.stat.StatisticManager;
import lombok.extern.slf4j.Slf4j;

//@Service
@Slf4j
public class KafkaSupportService extends BaseMainService {
    private ControlService controlService;
    private SessionManager sessionManager;

    public KafkaSupportService(ChartsDataRepository chartsDataRepository,
                               StatisticManager normalizedStatManager,
                               ExperimentManager experimentManager,
                               ControlService controlService,
                               SessionManager sessionManager) {
        super(chartsDataRepository, normalizedStatManager, experimentManager);
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