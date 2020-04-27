package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltDependentChart;
import com.mostovoy_company.expirement.chart_experiment.entity.Statistic;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(9)
public class MaxInterClusterHoleCountChart extends BaseLineChartData implements LightningBoltDependentChart {
    public MaxInterClusterHoleCountChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 0;
    }

    @Override
    public String getChartName() {
        return "Максимальное растояние между кластерами";
    }

    @Override
    public String getTabName() {
        return "Максимальное межкластерное расстояние";
    }

    @Override
    public void collectStatistic(ResponseMessage message, List<Statistic> statistics) {
        message.setMaxInterClusterIntervalSize(statistics.stream()
                .mapToDouble(Statistic::getMaxInterClusterHoleSize)
                .average()
                .orElse(0));

    }

    @Override
    public void parseResponseMessage(ResponseMessage message) {
        parseResponseMessageAndAdd(message, message.getMaxInterClusterIntervalSize());
    }
}
