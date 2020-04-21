package com.mostovoy_company.services;

import com.mostovoy_company.chart.LineChartData;
import com.mostovoy_company.expirement.ExperimentManager;
import com.mostovoy_company.services.kafka.dto.RequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DefaultService extends BaseMainService {


    public DefaultService(List<LineChartData> charts, ExperimentManager experimentManager) {
        super(charts, experimentManager);
    }

    @Override
    protected RequestMessage buildRequestMessage(int count, int size, double probability) {
        return RequestMessage.builder()
                             .count(count)
                             .size(size)
                             .probability(probability)
                             .build();
    }

    @Override
    public void consume(ConsumeProperties consumeProperties) {
        new Thread(() ->
        {
            long startTime = System.currentTimeMillis();
            RequestMessage message = getNextRequestMessage();
            List<RequestMessage> messages = new ArrayList<>();
            while (message != null) {
                messages.add(message);
                message = getNextRequestMessage();
            }
            messages.forEach(mes -> addDotsToCharts(performCalculation(mes, consumeProperties)));
            log.info("=> total time: " + (System.currentTimeMillis() - startTime));
        }).start();
    }

}