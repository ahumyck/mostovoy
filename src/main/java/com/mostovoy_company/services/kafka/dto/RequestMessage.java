package com.mostovoy_company.services.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestMessage {
    private long sessionId;
    private String nodeName;
    private int count;
    private int size;
    private double probability;
}
