package com.mostovoy_company.chart.impl;

import com.mostovoy_company.ChartConfigurationTab;
import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltDependChart;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

import static com.mostovoy_company.chart.ChartNames.WAY_LENGTHS_CHART;

@Component(WAY_LENGTHS_CHART)
public class WayLengthsChart extends BaseLineChartData implements LightningBoltDependChart {

    public WayLengthsChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

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
