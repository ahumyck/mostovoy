package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltDependChart;
import net.rgielen.fxweaver.core.FxWeaver;

//@Component(RATIO_DARK_RED_AND_BLACK_CELLS_CHART)
public class RatioDarkRedAndBlackCellsChart extends BaseLineChartData implements LightningBoltDependChart {

    public RatioDarkRedAndBlackCellsChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

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
