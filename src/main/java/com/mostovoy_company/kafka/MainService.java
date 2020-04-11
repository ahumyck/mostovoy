package com.mostovoy_company.kafka;

import com.mostovoy_company.kafka.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MainService {
    private ControlService controlService;
    private SessionManager sessionManager;

    @Autowired
    public MainService(ControlService controlService, SessionManager sessionManager) {
        this.controlService = controlService;
        this.sessionManager = sessionManager;
    }

    public void startNewSession() {
        controlService.sendStartMessage();
    }

    public void initNewSession() {
        sessionManager.initNewSession();
    }

    public void add(int count, int size, double probability) {
        sessionManager.getCurrentSessionData().addMessage(count, size, probability);
    }
}