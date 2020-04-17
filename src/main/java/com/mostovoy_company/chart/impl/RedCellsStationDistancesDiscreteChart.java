package com.mostovoy_company.chart.impl;

import com.mostovoy_company.ChartConfigurationTab;
import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltDependChart;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.RED_CELLS_STATION_DISTANCES_DISCRETE_CHART;

@Component(RED_CELLS_STATION_DISTANCES_DISCRETE_CHART)
public class RedCellsStationDistancesDiscreteChart extends BaseLineChartData implements LightningBoltDependChart {

    public RedCellsStationDistancesDiscreteChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1;
    }

    @Override
    public String getChartName() {
        return "Расстояние вычисляется как количество переходов";
    }

    @Override
    public String getTabName() {
        return "Расстояние установки способ 1";
    }
}
