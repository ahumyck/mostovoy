package com.mostovoy_company;

import com.mostovoy_company.chart.ChartNames;
import com.mostovoy_company.chart.ChartsDataRepository;
import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.filling.RandomFillingType;
import com.mostovoy_company.kafka.dto.LineChartNode;
import com.mostovoy_company.kafka.dto.ResponseMessage;
import com.mostovoy_company.stat.NormalizedStatManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

@Component
@Slf4j
public class DefaultService {

    private ChartsDataRepository chartsDataRepository;
    private NormalizedStatManager normalizedStatManager;

    public DefaultService(ChartsDataRepository chartsDataRepository, NormalizedStatManager normalizedStatManager) {
        this.chartsDataRepository = chartsDataRepository;
        this.normalizedStatManager = normalizedStatManager;
    }

    public void consume(int count, int size, double probability){
        log.info("=> start consume count: " + count + " size: " + size + " probability: " + probability);
        long startTime = System.currentTimeMillis();
        RandomFillingType fillingType = new RandomFillingType();
        fillingType.setPercolationProbability(probability);
        fillingType.setSize(size);
        ForkJoinPool forkJoinPool = new ForkJoinPool(6);
        List<Experiment> experiments = null;
        try {
            experiments = forkJoinPool.submit(() -> new ExperimentManager().initializeExperiments(count, fillingType)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Map<String, LineChartNode> values = new HashMap<>();
        values.put(ChartNames.CLUSTER_COUNT_CHART, buildLineChartNode(probability, normalizedStatManager.clusterCountStat(experiments)));
        values.put(ChartNames.CLUSTER_SIZE_CHART, buildLineChartNode(probability, normalizedStatManager.clusterSizeStat(experiments)));
        values.put(ChartNames.RED_CELLS_ADDED_CHART, buildLineChartNode(probability, normalizedStatManager.redCellsCountStat(experiments)));
        values.put(ChartNames.WAY_LENGTHS_CHART, buildLineChartNode(probability, normalizedStatManager.wayLengthStat(experiments)));
        values.put(ChartNames.RED_CELLS_STATION_DISTANCES_PI_CHART, buildLineChartNode(probability, normalizedStatManager.redCellStationDistanceForPythagoras(experiments)));
        values.put(ChartNames.RED_CELLS_STATION_DISTANCES_NE_PI_CHART, buildLineChartNode(probability, normalizedStatManager.redCellStationDistanceForDiscrete(experiments)));
        log.info("=> end consume count: " + count + " size: " + size + " probability: " + probability + " time:" + (System.currentTimeMillis() - startTime));
        Platform.runLater(()->{
            chartsDataRepository.addAll(size, values);
        });
    }
    private LineChartNode buildLineChartNode(double x, double y) {
        return new LineChartNode(x, y);
    }
}
