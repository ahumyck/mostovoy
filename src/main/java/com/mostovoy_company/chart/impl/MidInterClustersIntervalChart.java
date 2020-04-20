package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.ChartNames;
import com.mostovoy_company.chart.LightningBoltDependentChart;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component(ChartNames.MID_INTERCLUSTER_INTERVAL_SIZE)
@Order(8)
public class MidInterClustersIntervalChart extends BaseLineChartData implements LightningBoltDependentChart {

    public MidInterClustersIntervalChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0;
    }

    @Override
    public String getChartName() {
        return "Среднее растояние между кластерами";
    }

    @Override
    public String getTabName() {
        return "Межкластерное расстояние";
    }
}
