package com.mostovoy_company.services;

import com.mostovoy_company.chart.LineChartData;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.expirement.entity.Statistic;
import com.mostovoy_company.expirement.filling.RandomFillingType;
import com.mostovoy_company.services.kafka.dto.LineChartNode;
import com.mostovoy_company.services.kafka.dto.RequestMessage;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public abstract class BaseMainService implements MainService {

    private Queue<RequestMessage> queue = new ConcurrentLinkedQueue<>();
    private ExperimentManager experimentManager;
    private List<LineChartData> charts;

    protected BaseMainService(List<LineChartData> charts, ExperimentManager experimentManager) {
        this.experimentManager = experimentManager;
        this.charts = charts;
    }

    protected void addDotsToCharts(ResponseMessage message) {
        Platform.runLater(() -> charts.forEach(chartData -> chartData.parseResponseMessage(message)));
    }

    @Override
    public void addExperimentsDescription(int count, int size, double probability) {
        addMessage(buildRequestMessage(count, size, probability));
    }

    protected abstract RequestMessage buildRequestMessage(int number, int size, double probability);

    protected void addMessage(RequestMessage message) {
        queue.add(message);
    }

    protected Queue<RequestMessage> getQueue() {
        return queue;
    }

    protected RequestMessage getNextRequestMessage() {
        return queue.poll();
    }

    protected ResponseMessage performCalculation(RequestMessage message, ConsumeProperties consumeProperties) {
        return performCalculation(message.getSize(), message.getCount(), message.getProbability(), consumeProperties);
    }

    protected ResponseMessage performCalculation(int size, int count, double probability, ConsumeProperties consumeProperties) {
        long startTime = System.currentTimeMillis();
        RandomFillingType fillingType = new RandomFillingType();
        fillingType.setPercolationProbability(probability);
        fillingType.setSize(size);
        List<Statistic> statistics = experimentManager.getStatistics(count, fillingType, consumeProperties);
        log.info("=> end consumed request message: " + (System.currentTimeMillis() - startTime));
        return collectStatisticAndBuildResponseMessage(size, probability, statistics, consumeProperties);
    }

    private ResponseMessage collectStatisticAndBuildResponseMessage(int size, double probability, List<Statistic> statistics, ConsumeProperties consumeProperties) {
        ResponseMessage responseMessage = ResponseMessage.builder()
                                                         .size(size)
                                                         .probability(probability)
                                                         .build();
        charts.forEach(chartData -> chartData.collectStatistic(responseMessage, statistics));
        return responseMessage;
    }


//    private Map<String, LineChartNode> parseResponseMessage(ResponseMessage message) {
//        Map<String, LineChartNode> values = new HashMap<>();
//        putToValues(values, ChartNames.CLUSTER_COUNT_CHART, message.getClusterCount());
//        putToValues(values, ChartNames.CLUSTER_SIZE_CHART, message.getClusterSize());
//        putToValues(values, ChartNames.RED_CELLS_ADDED_CHART, message.getAddedRedCellCount());
//        putToValues(values, ChartNames.WAY_LENGTHS_CHART, message.getPercolationWayLength());
//        putToValues(values, ChartNames.RED_CELLS_STATION_DISTANCES_PYTHAGORAS_CHART, message.getRedCellsStationDistancesPythagoras());
//        putToValues(values, ChartNames.RED_CELLS_STATION_DISTANCES_DISCRETE_CHART, message.getRedCellsStationDistancesDiscrete());
//        putToValues(values, ChartNames.PERCOLATION_CHART, message.getPercolationThreshold());
//        putToValues(values, ChartNames.PERCOLATION_WAY_WIDTH_CHART, message.getPercolationWayWidth());
//        putToValues(values, ChartNames.MID_INTERCLUSTER_INTERVAL_SIZE, message.getInterClusterIntervalSize());
//        putToValues(values, ChartNames.INTER_CLUSTER_INTERVAL_HOLE_COUNT, message.getInterClustersHoleCount());
//        return values;
//    }

    private void putToValues(Map<String, LineChartNode> values, String chartName, LineChartNode value) {
        if (value != null) values.put(chartName, value);
    }
}