package com.mostovoy_company.services;

import com.mostovoy_company.chart.LineChartData;
import com.mostovoy_company.expirement.chart_experiment.ExperimentManager;
import com.mostovoy_company.expirement.chart_experiment.entity.Statistic;
import com.mostovoy_company.expirement.chart_experiment.filling.RandomFillingType;
import com.mostovoy_company.services.kafka.dto.RequestMessage;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
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

    protected abstract void addMessage(RequestMessage message);

    protected Queue<RequestMessage> getQueue() {
        return queue;
    }

    protected RequestMessage getNextRequestMessage() {
        return queue.poll();
    }

    protected ResponseMessage performCalculation(RequestMessage message, ConsumeProperties consumeProperties) {
        return performCalculation(message.getSize(), message.getCount(), message.getProbability(), consumeProperties);
    }

    private ResponseMessage performCalculation(int size, int count, double probability, ConsumeProperties consumeProperties) {
        long startTime = System.currentTimeMillis();
        RandomFillingType fillingType = new RandomFillingType();
        fillingType.setPercolationProbability(probability);
        fillingType.setSize(size);
        List<Statistic> statistics = experimentManager.getStatistics(count, fillingType, consumeProperties);
        log.info("=> end consumed request message: " + (System.currentTimeMillis() - startTime));
        return collectStatisticAndBuildResponseMessage(size, probability, statistics, consumeProperties);
    }

    private ResponseMessage collectStatisticAndBuildResponseMessage(int size, double probability, List<Statistic> statistics, ConsumeProperties consumeProperties) {
        ResponseMessage responseMessage = buildResponseMessage();
        responseMessage.setSize(size);
        responseMessage.setProbability(probability);
        charts.forEach(chartData -> chartData.collectStatistic(responseMessage, statistics));
        return responseMessage;
    }

    protected abstract ResponseMessage buildResponseMessage();
}