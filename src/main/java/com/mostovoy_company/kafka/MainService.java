package com.mostovoy_company.kafka;

import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.filling.RandomFillingType;
import com.mostovoy_company.kafka.dto.LineChartNode;
import com.mostovoy_company.kafka.dto.Message;
import com.mostovoy_company.kafka.dto.Response;
import com.mostovoy_company.stat.NormalizedStatManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

@Service
@Slf4j
public class MainService {

    private final KafkaTemplate<Long, Response> kafkaResponseTemplate;
    private final KafkaTemplate<Long, Message> kafkaMessageTemplate;
    private NormalizedStatManager normalizedStatManager;

    private Map<Integer, ObservableList<XYChart.Data>> midClustersCounts = new HashMap<>();
    private Map<Integer,ObservableList<XYChart.Data>> midClustersSize= new HashMap<>();
    private Map<Integer,ObservableList<XYChart.Data>> midRedCellsCount= new HashMap<>();
    private Map<Integer,ObservableList<XYChart.Data>>midWayLengths= new HashMap<>();
    private Map<Integer,ObservableList<XYChart.Data>> redCellsStationDistancesPythagoras= new HashMap<>();
    private Map<Integer,ObservableList<XYChart.Data>> redCellsStationDistancesDiscrete= new HashMap<>();

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

    @Autowired
    public MainService(KafkaTemplate<Long, Response> kafkaResponseTemplate, KafkaTemplate<Long, Message> kafkaMessageTemplate, NormalizedStatManager normalizedStatManager) {
        this.kafkaResponseTemplate = kafkaResponseTemplate;
        this.kafkaMessageTemplate = kafkaMessageTemplate;
        this.normalizedStatManager = normalizedStatManager;
    }

    @KafkaListener(topics = {"server.experiments"}, containerFactory = "messageFactory")
    public void consumeMessage(Message dto) throws ExecutionException, InterruptedException {
        log.info("=> start consume {}", dto);
        long startTime = System.currentTimeMillis();
        RandomFillingType fillingType = new RandomFillingType();
        fillingType.setPercolationProbability(dto.getProbability());
        fillingType.setSize(dto.getSize());
        ForkJoinPool forkJoinPool = new ForkJoinPool(6);
        int size = dto.getSize();
        double probability = dto.getProbability();
        List<Experiment> experiments = forkJoinPool.submit(() -> new ExperimentManager().initializeExperiments(dto.getCount(), fillingType)).get();
        kafkaResponseTemplate.send("server.response", Response.builder()
                .size(size)
                .midClustersCounts(buildLineChartNode(probability, normalizedStatManager.clusterCountStat(experiments)))
                .midClustersSize(buildLineChartNode(probability, normalizedStatManager.clusterSizeStat(experiments)))
                .midRedCellsCount(buildLineChartNode(probability, normalizedStatManager.redCellsCountStat(experiments)))
                .midWayLengths(buildLineChartNode(probability, normalizedStatManager.wayLengthStat(experiments)))
                .redCellsStationDistancesDiscrete(buildLineChartNode(probability, normalizedStatManager.redCellStationDistanceForDiscrete(experiments)))
                .redCellsStationDistancesPythagoras(buildLineChartNode(probability, normalizedStatManager.redCellStationDistanceForPythagoras(experiments)))
                .build());
        log.info("=> end consume time:{}, {}", System.currentTimeMillis() - startTime, dto);
    }

    @KafkaListener(topics = {"server.response"}, containerFactory = "responseFactory")
    public void consumeResponse(Response dto) throws ExecutionException, InterruptedException {
        log.info("=> consumed {}", dto);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                midClustersCounts.get(dto.getSize()).add(convertToXYChartData(dto.getMidClustersCounts()));
                midClustersSize.get(dto.getSize()).add(convertToXYChartData(dto.getMidClustersSize()));
                midRedCellsCount.get(dto.getSize()).add(convertToXYChartData(dto.getMidRedCellsCount()));
                midWayLengths.get(dto.getSize()).add(convertToXYChartData(dto.getMidWayLengths()));
                redCellsStationDistancesPythagoras.get(dto.getSize()).add(convertToXYChartData(dto.getRedCellsStationDistancesPythagoras()));
                redCellsStationDistancesDiscrete.get(dto.getSize()).add(convertToXYChartData(dto.getRedCellsStationDistancesDiscrete()));
            }
        });

//        log.info("insert to cache {}", experiments.size());
    }

    public void send(int count, int size, double percolation) {
        kafkaMessageTemplate.send("server.experiments", new Message(count, size, percolation));
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
