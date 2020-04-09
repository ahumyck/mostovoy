package com.mostovoy_company.kafka.session;

import com.mostovoy_company.kafka.dto.RequestMessage;

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

    public void addMessage(int count, int size, double probability){
        queue.add(new RequestMessage(sessionId,null, count, size, probability));
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
