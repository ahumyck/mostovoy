package com.mostovoy_company.expirement;

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
}