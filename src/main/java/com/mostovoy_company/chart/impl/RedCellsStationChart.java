package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltDependentChart;
import com.mostovoy_company.expirement.chart_experiment.entity.Statistic;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class RedCellsStationChart extends BaseLineChartData implements LightningBoltDependentChart {

    public RedCellsStationChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0 / size;
    }

    @Override
    public String getChartName() {
        return "Среднее расстояние установки темно-красной клетки";
    }

    @Override
    public String getTabName() {
        return "Расстояние установки";
    }

    @Override
    public void collectStatistic(ResponseMessage message, List<Statistic> statistics) {
        AtomicReference<Double> d = new AtomicReference<>(0.0);
        AtomicInteger n = new AtomicInteger(0);
        statistics.forEach(statistic -> {
            d.updateAndGet(v -> v + statistic.getMidDarkRedCellsStation() * statistic.getRelationsCounter());
            n.addAndGet(statistic.getRelationsCounter());
        });
        if (n.get() == 0) message.setRedCellsStationDistancesPythagoras(message.getSize() * message.getSize());
        else message.setRedCellsStationDistancesPythagoras(d.get() / n.get());
    }

    @Override
    public void parseResponseMessage(ResponseMessage message) {
        parseResponseMessageAndAdd(message, message.getRedCellsStationDistancesPythagoras());
    }
}
