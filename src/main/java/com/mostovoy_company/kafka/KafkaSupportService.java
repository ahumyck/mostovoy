package com.mostovoy_company.kafka;

import com.mostovoy_company.BaseMainService;
import com.mostovoy_company.chart.ChartsDataRepository;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.kafka.dto.RequestMessage;
import com.mostovoy_company.kafka.session.SessionManager;
import com.mostovoy_company.stat.NewNormalizedStatManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
@Slf4j
public class KafkaSupportService extends BaseMainService {
    private ControlService controlService;
    private SessionManager sessionManager;

    public KafkaSupportService(ChartsDataRepository chartsDataRepository,
                               NewNormalizedStatManager normalizedStatManager,
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
    public void consume() {
        initNewSession();
        startNewSession();
    }
}