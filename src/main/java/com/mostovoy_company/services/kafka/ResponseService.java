package com.mostovoy_company.services.kafka;

import com.mostovoy_company.chart.ChartNames;
import com.mostovoy_company.chart.ChartsDataRepository;
import com.mostovoy_company.services.kafka.dto.LineChartNode;
import com.mostovoy_company.services.kafka.dto.ResponseMessage;
import com.mostovoy_company.services.kafka.session.SessionManager;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

//@Component
@Slf4j
public class ResponseService {

    private KafkaTemplate<Long, ResponseMessage> kafkaResponseTemplate;
    private ChartsDataRepository chartsDataRepository;
    private SessionManager sessionManager;

    public ResponseService(KafkaTemplate<Long, ResponseMessage> kafkaResponseTemplate,
                           ChartsDataRepository chartsDataRepository,
                           SessionManager sessionManager) {
        this.kafkaResponseTemplate = kafkaResponseTemplate;
        this.chartsDataRepository = chartsDataRepository;
        this.sessionManager = sessionManager;
    }


    @KafkaListener(topics = {"server.response"}, containerFactory = "responseMessageKafkaListenerContainerFactory")
    public void consumeResponseMessage(ResponseMessage message) {
        if (message.getSessionId() == sessionManager.getCurrentSessionId()) {
            Platform.runLater(() -> chartsDataRepository.addAll(message.getSize(), parseResponseMessage(message)));
        }
        log.info("=> consumed response  {}", message);
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

    public void sendResponseMessage(ResponseMessage message) {
        kafkaResponseTemplate.send("server.response", message);
    }
}
