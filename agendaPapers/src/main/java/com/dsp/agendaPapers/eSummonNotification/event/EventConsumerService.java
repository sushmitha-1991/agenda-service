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
        
    @Autowired
    ESummonHistoryService esummonHistoryService;
    
    public static final Logger log = LoggerFactory.getLogger(EventConsumerService.class);

    @KafkaListener(topics = "esummon", groupId = "group_id")
    public void consumeEvent (String esummonId) {
        try {
            System.out.println("ESummon Consumer activated for ID : " + esummonId);
            Long id = Long.valueOf(esummonId);
            esummonHistoryService.sendESummonToAllMembers(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
