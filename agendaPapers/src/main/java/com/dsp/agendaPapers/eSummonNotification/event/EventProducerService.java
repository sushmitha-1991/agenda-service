package com.dsp.agendaPapers.eSummonNotification.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducerService {

    private static final Logger logger = LoggerFactory.getLogger(EventProducerService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.eSummon}")
    private String eSummonTopic;

    public void generateEvent(String eSummonId) {
        try {
            kafkaTemplate.send(eSummonTopic, eSummonId);
            logger.info("Sent eSummon Kafka event for ID: {}", eSummonId);
        } catch (Exception e) {
            logger.error("Failed to send Kafka event for eSummon ID: {}", eSummonId, e);
        }
    }
}
