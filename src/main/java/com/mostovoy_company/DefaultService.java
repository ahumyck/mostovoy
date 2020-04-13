package com.mostovoy_company;

import com.mostovoy_company.chart.ChartsDataRepository;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.kafka.dto.LineChartNode;
import com.mostovoy_company.kafka.dto.RequestMessage;
import com.mostovoy_company.kafka.dto.ResponseMessage;
import com.mostovoy_company.stat.NewNormalizedStatManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DefaultService extends BaseMainService {


    public DefaultService(ChartsDataRepository chartsDataRepository,
                          NewNormalizedStatManager normalizedStatManager,
                          ExperimentManager experimentManager) {
        super(chartsDataRepository, normalizedStatManager, experimentManager);
    }

    @Override
    protected RequestMessage buildRequestMessage(int count, int size, double probability) {
        return RequestMessage.builder()
                .count(count)
                .size(size)
                .probability(probability)
                .build();
    }

//    @Override
//    public void consume(){
//        log.info("=> start consume count: " + count + " size: " + size + " probability: " + probability);
//        long startTime = System.currentTimeMillis();
//        RandomFillingType fillingType = new RandomFillingType();
//        fillingType.setPercolationProbability(probability);
//        fillingType.setSize(size);
//        ForkJoinPool forkJoinPool = new ForkJoinPool(6);
//        List<Experiment> experiments = null;
//        try {
//            experiments = forkJoinPool.submit(() -> new ExperimentManager().initializeExperiments(count, fillingType)).get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        Map<String, LineChartNode> values = new HashMap<>();
//        values.put(ChartNames.CLUSTER_COUNT_CHART, buildLineChartNode(probability, normalizedStatManager.clusterCountStat(experiments)));
//        values.put(ChartNames.CLUSTER_SIZE_CHART, buildLineChartNode(probability, normalizedStatManager.clusterSizeStat(experiments)));
//        values.put(ChartNames.RED_CELLS_ADDED_CHART, buildLineChartNode(probability, normalizedStatManager.redCellsCountStat(experiments)));
//        values.put(ChartNames.WAY_LENGTHS_CHART, buildLineChartNode(probability, normalizedStatManager.wayLengthStat(experiments)));
//        values.put(ChartNames.RED_CELLS_STATION_DISTANCES_PI_CHART, buildLineChartNode(probability, normalizedStatManager.redCellStationDistanceForPythagoras(experiments)));
//        values.put(ChartNames.RED_CELLS_STATION_DISTANCES_NE_PI_CHART, buildLineChartNode(probability, normalizedStatManager.redCellStationDistanceForDiscrete(experiments)));
//        values.put(ChartNames.RATIO_DARK_RED_AND_BLACK_CELLS_CHART, buildLineChartNode(probability, normalizedStatManager.darkRedAndBlackCellsRatio(experiments)));
//        log.info("=> end consume count: " + count + " size: " + size + " probability: " + probability + " time:" + (System.currentTimeMillis() - startTime));
//        Platform.runLater(()->{
//            addDotsToCharts(size, values);
//        });
//    }

    @Override
    public void consume() {
        new Thread(() ->
        {
            long startTime = System.currentTimeMillis();
            RequestMessage message = getNextRequestMessage();
            List<RequestMessage> messages = new ArrayList<>();
            while (message != null) {
                messages.add(message);
                message = getNextRequestMessage();
            }
            messages.forEach(mes -> addDotsToCharts(performCalculation(mes)));
            log.info("=> total time: " + (System.currentTimeMillis() - startTime));
        }).start();
    }

}