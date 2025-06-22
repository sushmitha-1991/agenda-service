package com.dsp.agendaPapers.eSummonNotification.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EventProducer {

    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.eSummon}")
    private String eSummonTopic;

//    public void generateEvent(String eSummonId) {
//        try {
//            kafkaTemplate.send(eSummonTopic, eSummonId);
//            //get html content english, hindi , applicationid , using e sumonid
//            logger.info("Sent eSummon Kafka event for ID: {}", eSummonId);
//            System.out.println("#### generateEvent called");
//        } catch (Exception e) {
//            logger.error("Failed to send Kafka event for eSummon ID: {}", eSummonId, e);
//        }
//    }

//    public void generateEvent(Long eSummonId, String token) {
//        try {
//            // Create JSON payload
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Object> payload = new HashMap<>();
//            payload.put("eSummonId", eSummonId);
//            payload.put("token", token);
//
//            String jsonPayload = mapper.writeValueAsString(payload);
//            kafkaTemplate.send(eSummonTopic, jsonPayload);
//
//            logger.info("Sent eSummon Kafka event for ID: {}", eSummonId);
//        } catch (Exception e) {
//            logger.error("Failed to send Kafka event for eSummon ID: {}", eSummonId, e);
//        }
//    }
    
    public void generateEventV2(Long eSummonId, String token) {
        try {
            // Create JSON payload
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> payload = new HashMap<>();
            payload.put("eSummonId", eSummonId.toString());
            payload.put("token", token);

            String jsonPayload = mapper.writeValueAsString(payload);
            logger.info("Generating event for topic: {}", eSummonTopic);
            
            System.out.println("Sending eSummon Kafka event for key: {}" + eSummonTopic);
            kafkaTemplate.send(eSummonTopic, jsonPayload);
            System.out.println("Sent eSummon Kafka event for ID: {}" + eSummonId);

        } catch (Exception e) {
            logger.error("Failed to send Kafka event for eSummon ID: {}", eSummonId, e);
        }
    }

}
