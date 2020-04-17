package com.mostovoy_company.chart.impl;

import com.mostovoy_company.ChartConfigurationTab;
import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltDependChart;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.RED_CELLS_ADDED_CHART;

@Component(RED_CELLS_ADDED_CHART)
public class RedCellsAddedChart extends BaseLineChartData implements LightningBoltDependChart {

    public RedCellsAddedChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0 / size;
    }

    @Override
    public String getChartName() {
        return "Количество добавленых красных клеток";
    }

    @Override
    public String getTabName() {
        return "Количество красных клеток";
    }
}
