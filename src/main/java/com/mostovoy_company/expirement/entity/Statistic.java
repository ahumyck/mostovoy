package com.mostovoy_company.expirement.entity;

import com.mostovoy_company.expirement.lightningbolt.Paired;
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
    private int percolationWayLength;
    private Paired<Double,Integer> pythagorasDistance;
    private Paired<Double, Integer> discreteDistance;
    private boolean percolationizated;
    private int percolationWayWidth;
    private double midInterClustersInterval;
    private int interClustersHoleCount;
}