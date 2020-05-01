package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltDependentChart;
import com.mostovoy_company.expirement.chart_experiment.entity.Statistic;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlackCellsTapeChart extends BaseLineChartData implements LightningBoltDependentChart {
    public BlackCellsTapeChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0 / size;
    }

    @Override
    public String getChartName() {
        return "Среднее количество черных клеток в строке в прелах ширины управляемой перколяции";
    }

    @Override
    public String getTabName() {
        return "BlackCellsTapeChart";
    }

    @Override
    public void collectStatistic(ResponseMessage message, List<Statistic> statistics) {
        message.setBlackCellsTape(statistics.stream()
                .mapToDouble(Statistic::getAverageBlackCellsPerRowInWayWidth)
                .average()
                .orElse(0));
    }

    @Override
    public void parseResponseMessage(ResponseMessage message) {
        parseResponseMessageAndAdd(message, message.getBlackCellsTape());
    }
}
