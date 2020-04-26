package com.mostovoy_company.chart.impl;

import com.mostovoy_company.chart.BaseLineChartData;
import com.mostovoy_company.chart.LightningBoltIndependentChart;
import com.mostovoy_company.expirement.chart_experiment.entity.Statistic;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(3)
public class ClusterSizeChart extends BaseLineChartData implements LightningBoltIndependentChart {

    public ClusterSizeChart(FxWeaver fxWeaver) {
        super(fxWeaver);
    }

    @Override
    protected double getNormalizedCoefficient(int size) {
        return 1.0 / (size * size);
    }

    @Override
    public String getChartName() {
        return "Средний размер кластеров";
    }

    @Override
    public String getTabName() {
        return "Размер кластеров";
    }

    @Override
    public void collectStatistic(ResponseMessage message, List<Statistic> statistics) {
        double result = 0;
        final double[] blackCellCounter = {0};
        final double[] clusterCounter = {0};
        statistics.forEach(statistic -> {
            blackCellCounter[0] += statistic.getBlackCellCount();
            clusterCounter[0] += statistic.getClusterCount();
        });
        double concentration = blackCellCounter[0];
        if (clusterCounter[0] > 0) result = concentration / clusterCounter[0];
        message.setClusterSize(result);
    }

    @Override
    public void parseResponseMessage(ResponseMessage message) {
        parseResponseMessageAndAdd(message, message.getClusterSize());
    }
}
