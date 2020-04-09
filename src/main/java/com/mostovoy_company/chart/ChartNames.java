package com.mostovoy_company.chart;

import java.util.ArrayList;
import java.util.List;

public class ChartNames {

    private static final List<String> chartNames = new ArrayList<>();

    public static final String CLUSTER_COUNT_CHART = "clusterCountChart";
    public static final String CLUSTER_SIZE_CHART = "сlusterSizeChart";
    public static final String RED_CELLS_ADDED_CHART = "redCellsAddedChart";
    public static final String RED_CELLS_STATION_DISTANCES_NE_PI_CHART = "redCellsStationDistancesNePiChart";
    public static final String RED_CELLS_STATION_DISTANCES_PI_CHART = "redCellsStationDistancesPiChart";
    public static final String WAY_LENGTHS_CHART = "wayLengthsChart";

    static {
        chartNames.add(CLUSTER_COUNT_CHART);
        chartNames.add(CLUSTER_SIZE_CHART);
        chartNames.add(RED_CELLS_ADDED_CHART);
        chartNames.add(RED_CELLS_STATION_DISTANCES_NE_PI_CHART);
        chartNames.add(RED_CELLS_STATION_DISTANCES_PI_CHART);
        chartNames.add(WAY_LENGTHS_CHART);
    }

    public static List<String> getChartNames(){
        return chartNames;
    }
}
