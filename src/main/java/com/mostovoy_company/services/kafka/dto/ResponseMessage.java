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
    private LineChartNode midClustersCounts;
    private LineChartNode midClustersSize;
    private LineChartNode midRedCellsCount;
    private LineChartNode midWayLengths;
    private LineChartNode redCellsStationDistancesPythagoras;
    private LineChartNode redCellsStationDistancesDiscrete;
    private LineChartNode darkRedAndBlackCellsRatio;
    private LineChartNode percolationThreshold;
    private LineChartNode percolationWayWidth;
    private LineChartNode midInterClustersInterval;
    private LineChartNode interClustersHoleCount;
}
