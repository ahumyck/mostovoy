package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.RATIO_DARK_RED_AND_BLACK_CELLS_CHART;

@Component(RATIO_DARK_RED_AND_BLACK_CELLS_CHART)
public class RatioDarkRedAndBlackCellsChart extends BaseLineChartData {

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1;
    }

    @Override
    public String getChartName() {
        return "Отношение темнокрасных и черных клеток";
    }

    @Override
    public String getTabName() {
        return "Отношение темнокрасных и черных";
    }
}
