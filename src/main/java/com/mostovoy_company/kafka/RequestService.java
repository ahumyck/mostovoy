package com.mostovoy_company.kafka;


import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.expirement.Statistic;
import com.mostovoy_company.filling.RandomFillingType;
import com.mostovoy_company.kafka.dto.LineChartNode;
import com.mostovoy_company.kafka.dto.RequestMessage;
import com.mostovoy_company.kafka.dto.ResponseMessage;
import com.mostovoy_company.kafka.session.SessionManager;
import com.mostovoy_company.stat.NewNormalizedStatManager;
import com.mostovoy_company.stat.StatManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

@Slf4j
//@Component
public class RequestService {

    @Autowired
    private ControlService controlService;

    private SessionManager sessionManager;
    private NewNormalizedStatManager normalizedStatManager;
    private ResponseService responseService;
    private KafkaTemplate<Long, RequestMessage> kafkaRequestTemplate;

    @Autowired
    public RequestService(SessionManager sessionManager,
                          NewNormalizedStatManager normalizedStatManager,
                          ResponseService responseService,
                          KafkaTemplate<Long, RequestMessage> kafkaRequestTemplate) {
        this.responseService = responseService;
        this.sessionManager = sessionManager;
        this.normalizedStatManager = normalizedStatManager;
        this.kafkaRequestTemplate = kafkaRequestTemplate;
    }

    @KafkaListener(topics = "server.request",
            containerFactory = "requestMessageKafkaListenerContainerFactory",
            topicPartitions = @TopicPartition(topic = "server.request", partitions = SessionManager.NODE_NUMBER))
    public void consumeRequestMessage(final RequestMessage message) throws ExecutionException, InterruptedException {
        log.info("=>start consumed request message {}", message);
        performCalculationAndSendResponseInNewThread(message.getSize(), message.getCount(), message.getProbability());
    }

    private void performCalculationAndSendResponseInNewThread(int size, int count, double probability) {
        new Thread(() -> {
            try {
                performCalculationAndSendResponse(size, count, probability);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void performCalculationAndSendResponse(int size, int count, double probability) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        RandomFillingType fillingType = new RandomFillingType();
        fillingType.setPercolationProbability(probability);
        fillingType.setSize(size);
        ForkJoinPool forkJoinPool = new ForkJoinPool(8);
        List<Statistic> statistics = forkJoinPool.submit(
                () -> new ExperimentManager()
                        .getStatistics(count, fillingType))
                .get();
        responseService.sendResponseMessage(collectStatisticAndBuildResponseMessage(size, probability, statistics));
        controlService.sendReadyMessage();
        log.info("=> end consumed request message: " + (System.currentTimeMillis() - startTime));
    }

    private ResponseMessage collectStatisticAndBuildResponseMessage(int size, double probability, List<Statistic> statistics) {
        return ResponseMessage.builder()
                .sessionId(sessionManager.getCurrentSessionId())
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

    public void sendRequestMessage(RequestMessage message){
        kafkaRequestTemplate.send("server.request", message);
    }
}
