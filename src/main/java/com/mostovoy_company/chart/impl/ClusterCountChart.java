package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.CLUSTER_COUNT_CHART;

@Component(CLUSTER_COUNT_CHART)
public class ClusterCountChart extends BaseLineChartData {

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0 / (size * size);
    }

    @Override
    public String getChartName() {
        return "Зависимость количество кластеров от концентрации";
    }

    @Override
    public String getTabName() {
        return "Количество кластеров";
    }
}
