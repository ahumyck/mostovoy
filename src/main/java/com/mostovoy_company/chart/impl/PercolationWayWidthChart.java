package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltDependChart;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.PERCOLATION_WAY_WIDTH_CHART;

@Component(PERCOLATION_WAY_WIDTH_CHART)
@Order(6)
public class PercolationWayWidthChart extends BaseLineChartData implements LightningBoltDependChart {

    public PercolationWayWidthChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0 / size;
    }

    @Override
    public String getChartName() {
        return "Ширина перколяционного пути";
    }

    @Override
    public String getTabName() {
        return "Ширина пути";
    }
}
