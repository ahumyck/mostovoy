package com.mostovoy_company.expirement;

import com.mostovoy_company.lightning.Paired;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistic {
    private int size;
    private int redCellCount;
    private int clusterCount;
    private int blackCellCount;
    private int percolationWayDistance;
    private Paired<Double,Integer> pythagorasDistance;
    private Paired<Double, Integer> discreteDistance;
    private boolean percolationizated;
    private int percolationWayWidth;
    private double midInterClustersInterval;
    private int maxInterClusterInterval;
    private int interClustersHoleCount;
}