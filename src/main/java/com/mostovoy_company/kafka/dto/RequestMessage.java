package com.mostovoy_company.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMessage {
    private long sessionId;
    private String nodeName;
    private int count;
    private int size;
    private double probability;
}
