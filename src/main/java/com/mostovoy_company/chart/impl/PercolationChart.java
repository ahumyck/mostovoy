package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltDependChart;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.PERCOLATION_CHART;

@Component(PERCOLATION_CHART)
@Order(1)
public class PercolationChart extends BaseLineChartData implements LightningBoltDependChart {

    public PercolationChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0;
    }

    @Override
    public String getChartName() {
        return "Порог перколяция";
    }

    @Override
    public String getTabName() {
        return "Перколяция";
    }
}
