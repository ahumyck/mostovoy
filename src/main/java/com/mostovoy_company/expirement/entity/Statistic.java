package com.mostovoy_company.expirement.entity;

import com.google.gson.annotations.Expose;
import com.mostovoy_company.expirement.lightningbolt.Paired;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistic {
    @Expose
    private int size;
    @Expose
    private int redCellCount;
    @Expose
    private int clusterCount;
    @Expose
    private int blackCellCount;
    @Expose
    private int percolationWayLength;
    @Expose
    private Paired<Double,Integer> pythagorasDistance;
    @Expose
    private Paired<Double, Integer> discreteDistance;
    @Expose
    private boolean percolationizated;
    @Expose
    private int percolationWayWidth;
    @Expose
    private double midInterClustersInterval;
    @Expose
    private int interClustersHoleCount;
}