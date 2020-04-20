package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltDependentChart;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.RED_CELLS_STATION_DISTANCES_PYTHAGORAS_CHART;

@Component(RED_CELLS_STATION_DISTANCES_PYTHAGORAS_CHART)
public class RedCellsStationDistancesPythagorasChart extends BaseLineChartData implements LightningBoltDependentChart {

    public RedCellsStationDistancesPythagorasChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

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
