package com.mostovoy_company.services.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControlMessage {
    private long sessionId;
    private String nodeName;
    private String action;
}
