package com.mostovoy_company.kafka.session;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    public static final String START_SESSION_ACTION = "start";
    public static final String READY_ACTION = "ready";
    public static final String END_SESSION_ACTION = "end";
    public static final String NODE_NUMBER = "0";
    private static final String PREFIX_NODE_NAME = "node";
    public static final String NODE_NAME = PREFIX_NODE_NAME + NODE_NUMBER;


    private Map<Long, SessionData> sessionDataMap = new ConcurrentHashMap<>();
    private Random random = new Random();
    private long currentSessionId;

    public long getCurrentSessionId() {
        return currentSessionId;
    }

    public long initNewSession() {
        this.currentSessionId = random.nextLong();
        sessionDataMap.put(currentSessionId, new SessionData(currentSessionId, PREFIX_NODE_NAME + NODE_NUMBER, true));
        return currentSessionId;
    }

    public void updateSession(long sessionId) {
        this.currentSessionId = sessionId;
        sessionDataMap.put(sessionId, new SessionData(currentSessionId, PREFIX_NODE_NAME + NODE_NUMBER,false));
    }

    public SessionData getCurrentSessionData() {
        return sessionDataMap.get(currentSessionId);
    }

    public void closeSession(){

    }

    public String getNodeName(){
        return PREFIX_NODE_NAME + NODE_NUMBER;
    }
}
