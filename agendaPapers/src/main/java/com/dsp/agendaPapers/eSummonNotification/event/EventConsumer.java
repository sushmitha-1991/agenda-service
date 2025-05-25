package com.dsp.agendaPapers.eSummonNotification.event;

import com.dsp.agendaPapers.eSummonNotification.email.EmailService;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonHistory;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonHistoryRepository;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonNotificationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.dsp.agendaPapers.eSummonNotification.email.NumberConverterUtil.*;

@Slf4j
@Service
public class EventConsumer {

    @Autowired
    private ESummonHistoryRepository historyRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ESummonNotificationRepository notificationRepository;
    
    @Autowired
    @Lazy
    private RestTemplate restTemplate;    
    
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    

//    @KafkaListener(topics = "${kafka.topic.eSummon}", groupId = "eSummonGroup")
//    public void consume(String eSummonIdStr) {
//        log.info("Received Kafka event with eSummonId: {}", eSummonIdStr);
//System.out.println("#### Received Kafka event with eSummonId ");
//        Long eSummonId;
//        try {
//            eSummonId = Long.parseLong(eSummonIdStr);
//        } catch (NumberFormatException e) {
//            log.error("Invalid eSummonId received: {}", eSummonIdStr);
//            return;
//        }
//
//        Optional<ESummonNotification> notificationOpt = notificationRepository.findById(eSummonId);
//        System.out.println("### notificationOpt - " + notificationOpt);
//        if (notificationOpt.isEmpty()) {
//            log.error("No eSummonNotification found for ID: {}", eSummonId);
//            return;
//        }
//
//        ESummonNotification notification = notificationOpt.get();
//        int sessionNo = parseIntSafe(notification.getSessionNo());
//        int lokSabhaNo = parseIntSafe(notification.getLokSabhaNo());
//
//        String sessionRoman = toRoman(sessionNo);
//        String lokSabhaNoWord = toOrdinalWord(lokSabhaNo);
//
//        List<ESummonHistory> pendingEntries =
//                historyRepository.findByeSummon_IdAndMailStatus(eSummonId, "PENDING");
//         //logger
//        log.info("[Kafka-Consumer] Found {} pending eSummonHistory entries", pendingEntries.size());
//
//        for (ESummonHistory history : pendingEntries) {
//            try {
//                //logger
//                log.info("[Kafka-Consumer] Attempting to send email to {}", history.getEmailId());
//
//                emailService.sendEmail(history, sessionRoman, lokSabhaNoWord);
//                history.setMailStatus("SENT");
//                history.setRetryCount(history.getRetryCount() + 1);
//                historyRepository.save(history);
//                log.info("Email sent to member: {} and status updated to SENT", history.getEmailId());
//            } catch (Exception e) {
//                log.error("Failed to send email to member: {}. Error: {}", history.getEmailId(), e.getMessage());
//                history.setRetryCount(history.getRetryCount() + 1);
//                historyRepository.save(history);
//                // logger
//                log.warn("[Kafka-Consumer] Retry count incremented and saved for: {}", history.getEmailId());
//            }
//        }
//    }
//
//    private String toRoman(int number) {
//        String[] romans = {
//            "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X",
//            "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX"
//        };
//        return (number > 0 && number < romans.length) ? romans[number] : String.valueOf(number);
//    }
//
//    private String toOrdinalWord(int number) {
//        String[] ordinals = {
//            "", "First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh",
//            "Eighth", "Ninth", "Tenth", "Eleventh", "Twelfth", "Thirteenth", "Fourteenth",
//            "Fifteenth", "Sixteenth", "Seventeenth", "Eighteenth", "Nineteenth", "Twentieth"
//        };
//        return (number > 0 && number < ordinals.length) ? ordinals[number] : String.valueOf(number);
//    }
//
//    private int parseIntSafe(String value) {
//        try {
//            return Integer.parseInt(value);
//        } catch (NumberFormatException e) {
//            log.warn("Unable to parse number from '{}'", value);
//            return 0;
//        }
//    }

//    @KafkaListener(topics = "${kafka.topic.eSummon}", groupId = "eSummonGroup")
//    public void consume(String messageJson) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(messageJson);
//            Long eSummonId = root.get("eSummonId").asLong();
//            String token = root.get("token").asText();
//
//            log.info("Received Kafka event for eSummonId: {}", eSummonId);
//
//            Optional<ESummonNotification> notificationOpt = notificationRepository.findById(eSummonId);
//            if (notificationOpt.isEmpty()) {
//                log.error("No eSummonNotification found for ID: {}", eSummonId);
//                return;
//            }
//
//            ESummonNotification notification = notificationOpt.get();
//            int sessionNo = parseIntSafe(notification.getSessionNo());
//            int lokSabhaNo = parseIntSafe(notification.getLokSabhaNo());
//            String sessionRoman = toRoman(sessionNo);
//            String lokSabhaNoWord = toOrdinalWord(lokSabhaNo);
//
//            List<ESummonHistory> pendingEntries = historyRepository.findByeSummon_IdAndMailStatus(eSummonId, "PENDING");
//
//            for (ESummonHistory history : pendingEntries) {
//                try {
//                    emailService.sendEmail(history, sessionRoman, lokSabhaNoWord, token);
//                    history.setMailStatus("SENT");
//                    history.setRetryCount(history.getRetryCount() + 1);
//                    historyRepository.save(history);
//                } catch (Exception e) {
//                    log.error("Failed to send email to member: {}. Error: {}", history.getEmailId(), e.getMessage());
//                    history.setRetryCount(history.getRetryCount() + 1);
//                    historyRepository.save(history);
//                }
//            }
//
//        } catch (Exception ex) {
//            log.error("Error processing Kafka message: {}", ex.getMessage());
//        }
//    }

    
    @KafkaListener(topics = "${kafka.topic.eSummon}", groupId = "eSummonGroup")
    public void consume (String messageJson) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(messageJson);
            String id = root.get("eSummonId").asText();
            String token = root.get("token").asText();
        	log.info("Received Kafka event for eSummonId: {}", id);

        	Long eSummonId = Long.parseLong(id);
        	log.info("Long eSummonId: {}", eSummonId);

            Optional<ESummonNotification> notificationOpt = notificationRepository.findById(eSummonId);
            if (notificationOpt.isEmpty()) {
                log.error("No eSummonNotification found for ID: {}", eSummonId);
                return;
            }

            ESummonNotification notification = notificationOpt.get();
            int sessionNo = parseIntSafe(notification.getSessionNo());
            int lokSabhaNo = parseIntSafe(notification.getLokSabhaNo());
            String sessionRoman = toRoman(sessionNo);
            String lokSabhaNoWord = toOrdinalWord(lokSabhaNo);

            List<ESummonHistory> pendingEntries = historyRepository.findByeSummon_IdAndMailStatus(eSummonId, "PENDING");

            for (ESummonHistory history : pendingEntries) {
            	UUID docUuid = generateDocument(notification, history, token);
            	log.info("Generate document ID: {}", docUuid);

                try {
                    emailService.sendEmail(history, sessionRoman, lokSabhaNoWord, token);
                    history.setMailStatus("SENT");
                    history.setDocumentUuid(docUuid);
                    history.setRetryCount(history.getRetryCount() + 1);
                    historyRepository.save(history);
                } catch (Exception e) {
                    log.error("Failed to send email to member: {}. Error: {}", history.getEmailId(), e.getMessage());
                    history.setRetryCount(history.getRetryCount() + 1);
                    history.setDocumentUuid(docUuid);
                    historyRepository.save(history);
                }
            }

        } catch (Exception ex) {
        	ex.printStackTrace();
            log.error("Error processing Kafka message: {}", ex.getMessage());
        }
    }

        
    
    
	private UUID generateDocument(ESummonNotification esummon, ESummonHistory history, String token) {

		// ✅ Use the passed token directly (no need to extract from request)
		if (StringUtils.isEmpty(token)) {
			throw new RuntimeException("Missing or invalid Authorization token");
		}

		// ✅ Prepare the upload HTML request
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBearerAuth(token); // Use passed Bearer token

		System.out.println("##### dto.getTemplateContent() - " + esummon.getTemplateContent());
		MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
		formParams.add("htmlContentEn", esummon.getTemplateContent());
		formParams.add("startingPageNo", "1");
		formParams.add("applicationId", "24");

		HttpEntity<MultiValueMap<String, String>> formRequest = new HttpEntity<>(formParams, headers);

		ResponseEntity<String> uploadResponse;
		try {
			uploadResponse = restTemplate.postForEntity("https://dspstg.sansad.in/template/upload-html-english",
					formRequest, String.class);
		} catch (Exception e) {
			logger.debug ("### Document generation failed");
			throw new RuntimeException("Failed to upload HTML content: " + e.getMessage(), e);
		}

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode responseJson = objectMapper.readTree(uploadResponse.getBody());

			if (!responseJson.has("documentUuid") || responseJson.get("documentUuid").isNull()) {
				throw new RuntimeException("documentUuid not found in upload-html-english response");
			}

			UUID documentUuid = UUID.fromString(responseJson.get("documentUuid").asText());

			logger.debug ("### documentUuid - " + documentUuid);
			return documentUuid;
		} catch (Exception ex) {
			throw new RuntimeException("Failed to process upload-html-english response", ex);
		}
	}
}
