package com.mostovoy_company.services.kafka.session;

import com.mostovoy_company.services.kafka.dto.RequestMessage;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SessionData {

    private long sessionId;
    private String nodeName;
    private boolean isMaster;
    private Queue<RequestMessage> queue = new ConcurrentLinkedQueue<RequestMessage>();

    public SessionData(long sessionId, String nodeName, boolean isMaster) {
        this.sessionId = sessionId;
        this.nodeName = nodeName;
        this.isMaster = isMaster;
    }

    public void addMessage(RequestMessage message){
        queue.add(message);
    }

    public RequestMessage nextMessage(){
        return queue.poll();
    }

    public boolean isMaster() {
        return isMaster;
    }

    public String getNodeName(){
        return nodeName;
    }
}
