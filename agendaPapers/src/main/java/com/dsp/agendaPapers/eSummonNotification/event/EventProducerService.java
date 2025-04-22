package com.dsp.agendaPapers.eSummonNotification.event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class EventProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EventProducerService.class);

    @Value("${kafka.topic.esummon:esummon}")
    private String esummonTopic;

    public EventProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void generateEvent(String esummonId) {
        try {
            ListenableFuture<SendResult<String, String>> future = (ListenableFuture<SendResult<String, String>>) kafkaTemplate.send(esummonTopic, esummonId);

            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onSuccess(SendResult<String, String> result) {
                    logger.info("Kafka message [{}] sent successfully to topic [{}], partition [{}], offset [{}]",
                            esummonId,
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                }

                @Override
                public void onFailure(Throwable ex) {
                    logger.error("Failed to send Kafka message [{}] to topic [{}]: {}", esummonId, esummonTopic, ex.getMessage(), ex);
                }
            });

        } catch (Exception e) {
            logger.error("Exception occurred while sending Kafka message [{}]: {}", esummonId, e.getMessage(), e);
        }
    }

}

