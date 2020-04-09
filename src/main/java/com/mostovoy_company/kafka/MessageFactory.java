package com.mostovoy_company.kafka;

import com.mostovoy_company.kafka.dto.ControlMessage;
import com.mostovoy_company.kafka.dto.RequestMessage;
import com.mostovoy_company.kafka.dto.ResponseMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageFactory {

    public ControlMessage getControlMessage() {
        return null;
    }

    public RequestMessage getRequestMessage() {
        return null;
    }

    public ResponseMessage getResponseMessage() {
        return null;
    }
}
