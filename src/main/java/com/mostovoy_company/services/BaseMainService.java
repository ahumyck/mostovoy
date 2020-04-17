package com.mostovoy_company.services;

import com.mostovoy_company.chart.ChartNames;
import com.mostovoy_company.chart.ChartsDataRepository;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.expirement.Statistic;
import com.mostovoy_company.filling.RandomFillingType;
import com.mostovoy_company.services.kafka.dto.LineChartNode;
import com.mostovoy_company.services.kafka.dto.RequestMessage;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import com.mostovoy_company.stat.StatisticManager;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public abstract class BaseMainService implements MainService {

    private ChartsDataRepository chartsDataRepository;
    private Queue<RequestMessage> queue = new ConcurrentLinkedQueue<>();
    private StatisticManager normalizedStatManager;
    private ExperimentManager experimentManager;

    protected BaseMainService(ChartsDataRepository chartsDataRepository, StatisticManager normalizedStatManager, ExperimentManager experimentManager) {
        this.chartsDataRepository = chartsDataRepository;
        this.normalizedStatManager = normalizedStatManager;
        this.experimentManager = experimentManager;
    }

    protected void addDotsToCharts(ResponseMessage message) {
        chartsDataRepository.addAll(message.getSize(), parseResponseMessage(message));
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
        return collectStatisticAndBuildResponseMessage(size, probability, statistics);
    }

    private ResponseMessage collectStatisticAndBuildResponseMessage(int size, double probability, List<Statistic> statistics) {
        return ResponseMessage.builder()
                .size(size)
                .midClustersCounts(buildLineChartNode(probability, normalizedStatManager.clusterCountStat(statistics)))
                .midClustersSize(buildLineChartNode(probability, normalizedStatManager.clusterSizeStat(statistics)))
                .midRedCellsCount(buildLineChartNode(probability, normalizedStatManager.redCellsCountStat(statistics)))
                .midWayLengths(buildLineChartNode(probability, normalizedStatManager.wayLengthStat(statistics)))
                .redCellsStationDistancesDiscrete(buildLineChartNode(probability, normalizedStatManager.redCellStationDistanceForDiscrete(statistics)))
                .redCellsStationDistancesPythagoras(buildLineChartNode(probability, normalizedStatManager.redCellStationDistanceForPythagoras(statistics)))
                .darkRedAndBlackCellsRatio(buildLineChartNode(probability, normalizedStatManager.darkRedAndBlackCellsRatio(statistics)))
                .build();
    }

    private LineChartNode buildLineChartNode(double x, double y) {
        return new LineChartNode(x, y);
    }

    private Map<String, LineChartNode> parseResponseMessage(ResponseMessage message) {
        Map<String, LineChartNode> values = new HashMap<>();
        values.put(ChartNames.CLUSTER_COUNT_CHART, message.getMidClustersCounts());
        values.put(ChartNames.CLUSTER_SIZE_CHART, message.getMidClustersSize());
        values.put(ChartNames.RED_CELLS_ADDED_CHART, message.getMidRedCellsCount());
        values.put(ChartNames.WAY_LENGTHS_CHART, message.getMidWayLengths());
        values.put(ChartNames.RED_CELLS_STATION_DISTANCES_PYTHAGORAS_CHART, message.getRedCellsStationDistancesPythagoras());
        values.put(ChartNames.RED_CELLS_STATION_DISTANCES_DISCRETE_CHART, message.getRedCellsStationDistancesDiscrete());
        return values;
    }
}
