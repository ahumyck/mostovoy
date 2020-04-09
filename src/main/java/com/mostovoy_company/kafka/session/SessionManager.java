package com.mostovoy_company.kafka.session;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String START_SESSION_ACTION = "start";
    public static final String READY_ACTION = "ready";
    public static final String END_SESSION_ACTION = "end";

    private Map<Long, SessionData> sessionDataMap = new ConcurrentHashMap<>();
    private Random random = new Random();
    private long currentSessionId;

    public long getCurrentSessionId() {
        return currentSessionId;
    }

    public long initNewSession() {
        this.currentSessionId = random.nextLong();
        sessionDataMap.put(currentSessionId, new SessionData(currentSessionId, "node0", true));
        return currentSessionId;
    }

    public void updateSession(long sessionId) {
        this.currentSessionId = sessionId;
        sessionDataMap.put(sessionId, new SessionData(currentSessionId, "node0",false));
    }

    public SessionData getCurrentSessionData() {
        return sessionDataMap.get(currentSessionId);
    }

    public void closeSession(){

    }
}
