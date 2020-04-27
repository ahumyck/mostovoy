package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltDependentChart;
import com.mostovoy_company.expirement.chart_experiment.entity.Statistic;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import net.rgielen.fxweaver.core.FxWeaver;

import java.util.List;

//@Component
public class RatioDarkRedAndBlackCellsChart extends BaseLineChartData implements LightningBoltDependentChart {

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

    @Override
    public void collectStatistic(ResponseMessage message, List<Statistic> statistics) {

    }

    @Override
    public void parseResponseMessage(ResponseMessage message) {

    }
}
