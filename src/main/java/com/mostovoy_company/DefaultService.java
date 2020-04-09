package com.mostovoy_company;

import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.filling.RandomFillingType;
import com.mostovoy_company.kafka.dto.LineChartNode;
import com.mostovoy_company.kafka.dto.Response;
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
    private NormalizedStatManager normalizedStatManager;
    private Map<Integer, ObservableList<XYChart.Data>> midClustersCounts = new HashMap<>();
    private Map<Integer,ObservableList<XYChart.Data>> midClustersSize= new HashMap<>();
    private Map<Integer,ObservableList<XYChart.Data>> midRedCellsCount= new HashMap<>();
    private Map<Integer,ObservableList<XYChart.Data>> midWayLengths= new HashMap<>();
    private Map<Integer,ObservableList<XYChart.Data>> redCellsStationDistancesPythagoras= new HashMap<>();
    private Map<Integer,ObservableList<XYChart.Data>> redCellsStationDistancesDiscrete= new HashMap<>();
    private Map<Integer,ObservableList<XYChart.Data>> darkRedAndBlackCellsRatio = new HashMap<>();

    public DefaultService(NormalizedStatManager normalizedStatManager) {
        this.normalizedStatManager = normalizedStatManager;
    }

    public void putDarkRedAndBlackCellsRatioStation(int size, ObservableList<XYChart.Data> darkRedAndBlackCellsRatioStation) {
        this.darkRedAndBlackCellsRatio.put(size, darkRedAndBlackCellsRatioStation);
    }

    public void putMidClustersCounts(int size, ObservableList<XYChart.Data> midClustersCounts) {
        this.midClustersCounts.put(size, midClustersCounts);
    }

    public void putMidClustersSize(int size, ObservableList<XYChart.Data> midClustersSize) {
        this.midClustersSize.put(size, midClustersSize);
    }

    public void putMidRedCellsCount(int size, ObservableList<XYChart.Data> midRedCellsCount) {
        this.midRedCellsCount.put(size, midRedCellsCount);
    }

    public void putMidWayLengths(int size, ObservableList<XYChart.Data> midWayLengths) {
        this.midWayLengths.put(size, midWayLengths);
    }

    public void putRedCellsStationDistancesPythagoras(int size, ObservableList<XYChart.Data> redCellsStationDistancesPythagoras) {
        this.redCellsStationDistancesPythagoras.put(size, redCellsStationDistancesPythagoras);
    }

    public void putRedCellsStationDistancesDiscrete(int size, ObservableList<XYChart.Data> redCellsStationDistancesDiscrete) {
        this.redCellsStationDistancesDiscrete.put(size, redCellsStationDistancesDiscrete);
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
        Response dto = Response.builder()
                .size(size)
                .midClustersCounts(buildLineChartNode(probability, normalizedStatManager.clusterCountStat(experiments)))
                .midClustersSize(buildLineChartNode(probability, normalizedStatManager.clusterSizeStat(experiments)))
                .midRedCellsCount(buildLineChartNode(probability, normalizedStatManager.redCellsCountStat(experiments)))
                .midWayLengths(buildLineChartNode(probability, normalizedStatManager.wayLengthStat(experiments)))
                .redCellsStationDistancesDiscrete(buildLineChartNode(probability, normalizedStatManager.redCellStationDistanceForDiscrete(experiments)))
                .redCellsStationDistancesPythagoras(buildLineChartNode(probability, normalizedStatManager.redCellStationDistanceForPythagoras(experiments)))
                .darkRedAndBlackCellsRatio(buildLineChartNode(probability, normalizedStatManager.darkRedAndBlackCellsRatio(experiments)))
                .build();
        log.info("=> end consume count: " + count + " size: " + size + " probability: " + probability + " time:" + (System.currentTimeMillis() - startTime));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                midClustersCounts.get(dto.getSize()).add(convertToXYChartData(dto.getMidClustersCounts()));
                midClustersSize.get(dto.getSize()).add(convertToXYChartData(dto.getMidClustersSize()));
                midRedCellsCount.get(dto.getSize()).add(convertToXYChartData(dto.getMidRedCellsCount()));
                midWayLengths.get(dto.getSize()).add(convertToXYChartData(dto.getMidWayLengths()));
                redCellsStationDistancesPythagoras.get(dto.getSize()).add(convertToXYChartData(dto.getRedCellsStationDistancesPythagoras()));
                redCellsStationDistancesDiscrete.get(dto.getSize()).add(convertToXYChartData(dto.getRedCellsStationDistancesDiscrete()));
                darkRedAndBlackCellsRatio.get(dto.getSize()).add(convertToXYChartData(dto.getDarkRedAndBlackCellsRatio()));
            }
        });
    }

    private XYChart.Data convertToXYChartData(LineChartNode node) {
        XYChart.Data dot = new XYChart.Data(node.x, node.y);
        Rectangle rect = new Rectangle(0, 0);
        rect.setVisible(false);
        dot.setNode(rect);
        return dot;
    }


    private LineChartNode buildLineChartNode(double x, double y) {
        return new LineChartNode(x, y);
    }
}
