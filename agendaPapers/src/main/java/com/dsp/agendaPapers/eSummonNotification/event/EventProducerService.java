package com.dsp.agendaPapers.eSummonNotification.event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducerService {  

    private final KafkaTemplate <String, String> kafkaTemplate;

    public EventProducerService(KafkaTemplate <String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void generateEvent(String esummonId) {
        try {
            kafkaTemplate.send("esummon", esummonId);
            System.out.println("Message sent: " + esummonId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }    
}

