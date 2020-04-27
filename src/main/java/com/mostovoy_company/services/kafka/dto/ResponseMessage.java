package com.mostovoy_company.services.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMessage {
    private long sessionId;
    private int size;
    private double probability;
    private double clusterCount;
    private double clusterSize;
    private double addedRedCellCount;
    private double percolationWayLength;
    private double redCellsStationDistancesPythagoras;
    private double redCellsStationDistancesDiscrete;
    private double percolationThreshold;
    private double percolationWayWidth;
    private double midInterClusterIntervalSize;
    private double maxInterClusterIntervalSize;
    private double interClustersHoleCount;
    private double blackCellsTape;
    private double darkRedCellsTape;
}
