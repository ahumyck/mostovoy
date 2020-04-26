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
@Order(8)
public class MidInterClustersIntervalSizeChart extends BaseLineChartData implements LightningBoltDependentChart {

    public MidInterClustersIntervalSizeChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0;
    }

    @Override
    public String getChartName() {
        return "Среднее растояние между кластерами";
    }

    @Override
    public String getTabName() {
        return "Межкластерное расстояние";
    }

    @Override
    public void collectStatistic(ResponseMessage message, List<Statistic> statistics) {
        message.setInterClusterIntervalSize(statistics.stream()
                .mapToDouble(Statistic::getMidInterClustersInterval)
                .average()
                .orElse(0));

    }

    @Override
    public void parseResponseMessage(ResponseMessage message) {
        parseResponseMessageAndAdd(message, message.getInterClusterIntervalSize());
    }
}
