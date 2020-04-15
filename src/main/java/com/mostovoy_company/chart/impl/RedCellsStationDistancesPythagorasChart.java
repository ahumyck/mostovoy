package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.RED_CELLS_STATION_DISTANCES_PYTHAGORAS_CHART;

@Component(RED_CELLS_STATION_DISTANCES_PYTHAGORAS_CHART)
public class RedCellsStationDistancesPythagorasChart extends BaseLineChartData {

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1;
    }

    @Override
    public String getChartName() {
        return "Расстояние вычисляется с помощью теоремы Пифагора";
    }

    @Override
    public String getTabName() {
        return "Расстояние установки способ 2";
    }
}
