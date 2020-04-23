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
@Order(1)
public class PercolationThresholdChart extends BaseLineChartData implements LightningBoltDependentChart {

    public PercolationThresholdChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 100;
    }

    @Override
    public String getChartName() {
        return "Порог перколяция";
    }

    @Override
    public String getTabName() {
        return "Перколяция";
    }

    @Override
    public void collectStatistic(ResponseMessage message, List<Statistic> statistics) {
        message.setPercolationThreshold(statistics.stream()
                                                  .mapToInt(statistic -> statistic.isPercolationizated() ? 1 : 0)
                                                  .average()
                                                  .orElse(0));
    }

    @Override
    public void parseResponseMessage(ResponseMessage message) {
        parseResponseMessageAndAdd(message, message.getPercolationThreshold());
    }
}
