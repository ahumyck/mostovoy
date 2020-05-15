package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltDependentChart;
import com.mostovoy_company.expirement.chart_experiment.entity.Statistic;
import com.mostovoy_company.expirement.chart_experiment.entity.StatisticManager;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class RedCellsStationDistancesDiscreteChart extends BaseLineChartData implements LightningBoltDependentChart {

    public RedCellsStationDistancesDiscreteChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0 / size;
    }

    @Override
    public String getChartName() {
        return "Среднее расстояние установки зеленой клетки вычисляется как количество переходов между ячейками";
    }

    @Override
    public String getTabName() {
        return "Расстояние установки дискретный";
    }

    @Override
    public void collectStatistic(ResponseMessage message, List<Statistic> statistics) {
        message.setRedCellsStationDistancesDiscrete(StatisticManager.redCellsStationDiscrete(statistics));
    }

    @Override
    public void parseResponseMessage(ResponseMessage message) {
        parseResponseMessageAndAdd(message, message.getRedCellsStationDistancesDiscrete());
    }
}