package com.mostovoy_company.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {
    private int size;
    private LineChartNode midClustersCounts;
    private LineChartNode midClustersSize;
    private LineChartNode midRedCellsCount;
    private LineChartNode midWayLengths;
    private LineChartNode redCellsStationDistancesPythagoras;
    private LineChartNode redCellsStationDistancesDiscrete;
}
