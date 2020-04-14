package com.mostovoy_company.services.kafka;

import com.mostovoy_company.services.kafka.dto.ControlMessage;
import com.mostovoy_company.services.kafka.dto.RequestMessage;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
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
