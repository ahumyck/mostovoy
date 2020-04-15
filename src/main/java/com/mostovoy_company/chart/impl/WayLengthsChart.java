package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.WAY_LENGTHS_CHART;

@Component(WAY_LENGTHS_CHART)
public class WayLengthsChart extends BaseLineChartData {

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0 / size;
    }

    @Override
    public String getChartName() {
        return "Средняя длина пути";
    }

    @Override
    public String getTabName() {
        return "Длина пути";
    }
}
