package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.CLUSTER_SIZE_CHART;

@Component(CLUSTER_SIZE_CHART)
public class ClusterSizeChart extends BaseLineChartData {

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0 / size;
    }

    @Override
    public String getChartName() {
        return "Средний размер кластеров";
    }

    @Override
    public String getTabName() {
        return "Размер кластеров";
    }
}
