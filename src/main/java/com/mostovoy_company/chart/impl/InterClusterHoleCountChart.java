package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.ChartNames;
import com.mostovoy_company.chart.LightningBoltDependChart;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component(ChartNames.INTER_CLUSTER_INTERVAL_HOLE_COUNT)
@Order(7)
public class InterClusterHoleCountChart extends BaseLineChartData implements LightningBoltDependChart {

    public InterClusterHoleCountChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0 / size;
    }

    @Override
    public String getChartName() {
        return "Среднее количество межкластерных дырок";
    }

    @Override
    public String getTabName() {
        return "Межкластерные дырки";
    }
}
