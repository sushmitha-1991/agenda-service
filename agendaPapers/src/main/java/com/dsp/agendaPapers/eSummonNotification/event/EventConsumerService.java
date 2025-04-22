package com.dsp.agendaPapers.eSummonNotification.event;

import com.dsp.agendaPapers.eSummonNotification.service.ESummonHistoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Component
public class EventConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumerService.class);

    @Autowired
    private ESummonHistoryService esummonHistoryService;

    @KafkaListener(topics = "${kafka.topic.esummon:esummon}", groupId = "${kafka.consumer.group-id:group_id}")
    public void consumeEvent(String esummonId) {
        logger.info("Kafka Consumer received message: {}", esummonId);
        try {
            Long id = Long.parseLong(esummonId);
            esummonHistoryService.sendESummonToAllMembers(id);
        } catch (NumberFormatException nfe) {
            logger.error("Invalid ESummon ID received: {}", esummonId, nfe);
        } catch (Exception e) {
            logger.error("Error while processing eSummon ID [{}]: {}", esummonId, e.getMessage(), e);
        }
    }


}
