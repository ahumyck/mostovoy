package com.mostovoy_company.kafka;

import com.mostovoy_company.chart.ChartNames;
import com.mostovoy_company.chart.ChartsDataRepository;
import com.mostovoy_company.expirement.Experiment;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.filling.RandomFillingType;
import com.mostovoy_company.kafka.dto.ControlMessage;
import com.mostovoy_company.kafka.dto.LineChartNode;
import com.mostovoy_company.kafka.dto.RequestMessage;
import com.mostovoy_company.kafka.dto.ResponseMessage;
import com.mostovoy_company.kafka.session.SessionManager;
import com.mostovoy_company.stat.NormalizedStatManager;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class MainService {

    private SessionManager sessionManager;
    private final KafkaTemplate<Long, ResponseMessage> kafkaResponseTemplate;
    private final KafkaTemplate<Long, RequestMessage> kafkaRequestTemplate;
    private final KafkaTemplate<Long, ControlMessage> kafkaControlTemplate;
    private NormalizedStatManager normalizedStatManager;
    private ChartsDataRepository chartsDataRepository;

    @Autowired
    public MainService(SessionManager sessionManager,
                       KafkaTemplate<Long, ResponseMessage> kafkaResponseTemplate,
                       KafkaTemplate<Long, RequestMessage> kafkaRequestTemplate,
                       KafkaTemplate<Long, ControlMessage> kafkaControlTemplate,
                       NormalizedStatManager normalizedStatManager,
                       ChartsDataRepository chartsDataRepository) {
        this.sessionManager = sessionManager;
        this.kafkaResponseTemplate = kafkaResponseTemplate;
        this.kafkaRequestTemplate = kafkaRequestTemplate;
        this.kafkaControlTemplate = kafkaControlTemplate;
        this.normalizedStatManager = normalizedStatManager;
        this.chartsDataRepository = chartsDataRepository;
    }

    @KafkaListener(topics = {"server.request"}, containerFactory = "requestMessageKafkaListenerContainerFactory")
    public void consumeRequestMessage(RequestMessage message) throws ExecutionException, InterruptedException {
        log.info("=> start consume request message {}", message);
        long startTime = System.currentTimeMillis();
        RandomFillingType fillingType = new RandomFillingType();
        fillingType.setPercolationProbability(message.getProbability());
        fillingType.setSize(message.getSize());
//        ForkJoinPool forkJoinPool = new ForkJoinPool(7);
        int size = message.getSize();
        double probability = message.getProbability();
        List<Experiment> experiments = /*forkJoinPool.submit(() -> */new ExperimentManager().initializeExperiments(message.getCount(), fillingType)/*).get()*/;
        kafkaResponseTemplate.send("server.response", ResponseMessage.builder()
                .sessionId(sessionManager.getCurrentSessionId())
                .size(size)
                .midClustersCounts(buildLineChartNode(probability, normalizedStatManager.clusterCountStat(experiments)))
                .midClustersSize(buildLineChartNode(probability, normalizedStatManager.clusterSizeStat(experiments)))
                .midRedCellsCount(buildLineChartNode(probability, normalizedStatManager.redCellsCountStat(experiments)))
                .midWayLengths(buildLineChartNode(probability, normalizedStatManager.wayLengthStat(experiments)))
                .redCellsStationDistancesDiscrete(buildLineChartNode(probability, normalizedStatManager.redCellStationDistanceForDiscrete(experiments)))
                .redCellsStationDistancesPythagoras(buildLineChartNode(probability, normalizedStatManager.redCellStationDistanceForPythagoras(experiments)))
                .build());
        sendReadyMessage();
        log.info("=> end consume request message time:{}, {}", System.currentTimeMillis() - startTime, message);
    }

    @KafkaListener(topics = {"server.response"}, containerFactory = "responseMessageKafkaListenerContainerFactory")
    public void consumeResponseMessage(ResponseMessage message) {
        if(message.getSessionId() == sessionManager.getCurrentSessionId()) {
            Platform.runLater(() -> {
                chartsDataRepository.addAll(message.getSize(), parseResponseMessage(message));
            });
        }
        log.info("=> consumed response message {}", message);
    }

    @KafkaListener(topics = {"server.control"}, containerFactory = "controlMessageKafkaListenerContainerFactory")
    public void consumeControlMessage(ControlMessage message) {
        if (message.getAction().equals(SessionManager.START_SESSION_ACTION)) {
            if (sessionManager.getCurrentSessionId() != message.getSessionId()) {
                sessionManager.updateSession(message.getSessionId());
                chartsDataRepository.clear();
            }
            sendReadyMessage();
        } else if (message.getAction().equals(SessionManager.READY_ACTION)) {
            if (message.getSessionId() == sessionManager.getCurrentSessionId() && sessionManager.getCurrentSessionData().isMaster()) {
                RequestMessage response = sessionManager.getCurrentSessionData().nextMessage();
                if(response == null)
                    kafkaControlTemplate.send("server.control", new ControlMessage(sessionManager.getCurrentSessionId(), "node0", SessionManager.END_SESSION_ACTION));
                else kafkaRequestTemplate.send("server.request", response);
            }
        } else if (message.getAction().equals(SessionManager.END_SESSION_ACTION))
        {
            sessionManager.closeSession();
        }
        log.info("=> consumed control message {}", message);
    }

    public void startNewSession() {
        kafkaControlTemplate.send("server.control", new ControlMessage(sessionManager.initNewSession(),"node0", SessionManager.START_SESSION_ACTION));
    }

    public void initNewSession() {
        sessionManager.initNewSession();
    }

    public void add(int count, int size, double probability) {
        sessionManager.getCurrentSessionData().addMessage(count, size, probability);
    }

    private void sendReadyMessage(){
        kafkaControlTemplate.send("server.control", new ControlMessage(sessionManager.getCurrentSessionId(), "node0", SessionManager.READY_ACTION));
    }

    private Map<String, LineChartNode> parseResponseMessage(ResponseMessage message) {
        Map<String, LineChartNode> values = new HashMap<>();
        values.put(ChartNames.CLUSTER_COUNT_CHART, message.getMidClustersCounts());
        values.put(ChartNames.CLUSTER_SIZE_CHART, message.getMidClustersSize());
        values.put(ChartNames.RED_CELLS_ADDED_CHART, message.getMidRedCellsCount());
        values.put(ChartNames.WAY_LENGTHS_CHART, message.getMidWayLengths());
        values.put(ChartNames.RED_CELLS_STATION_DISTANCES_PI_CHART, message.getRedCellsStationDistancesPythagoras());
        values.put(ChartNames.RED_CELLS_STATION_DISTANCES_NE_PI_CHART, message.getRedCellsStationDistancesDiscrete());
        return values;
    }

    private LineChartNode buildLineChartNode(double x, double y) {
        return new LineChartNode(x, y);
    }
}