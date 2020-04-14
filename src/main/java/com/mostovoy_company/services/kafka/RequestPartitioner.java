package com.mostovoy_company.services.kafka;

import com.mostovoy_company.services.kafka.dto.RequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RequestPartitioner implements Partitioner {

    private static Map<String,Integer> nameToPartitionMap;

    // This method will gets called at the start, you should use it to do one time startup activity
    public void configure(Map<String, ?> configs) {
        nameToPartitionMap = new HashMap<String, Integer>();
        for(Map.Entry<String,?> entry: configs.entrySet()){
            if(entry.getKey().startsWith("partitions.")){
                String keyName = entry.getKey();
                String value = (String)entry.getValue();
                System.out.println( keyName.substring(11));
                int partitionId = Integer.parseInt(keyName.substring(11));
                nameToPartitionMap.put(value,partitionId);
            }
        }
        log.info("partitions map: " + nameToPartitionMap);
    }

    //This method will get called once for each message
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        return nameToPartitionMap.getOrDefault(((RequestMessage) value).getNodeName(), 0);
    }
    // This method will get called at the end and gives your partitioner class chance to cleanup
    public void close() {}
}