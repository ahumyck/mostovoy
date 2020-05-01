package com.mostovoy_company.expirement.chart_experiment.entity;

import com.google.gson.annotations.Expose;
import com.mostovoy_company.expirement.chart_experiment.lightningbolt.Paired;
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
    private int relationsCounter;
    @Expose
    private double midGreenCellsStation;

    @Expose
    private boolean percolationizated;
    @Expose
    private int percolationWayWidth;
    @Expose
    private double midInterClustersInterval;
    @Expose
    private double maxInterClusterHoleSize;
    @Expose
    private int interClustersHoleCount;
    @Expose
    private double averageBlackCellsPerRowInWayWidth;
    @Expose
    private double averageRedCellsPerRowInTapeWidth;

}