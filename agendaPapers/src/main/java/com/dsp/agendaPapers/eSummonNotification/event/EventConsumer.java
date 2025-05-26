package com.dsp.agendaPapers.eSummonNotification.event;

import com.dsp.agendaPapers.eSummonNotification.email.EmailService;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonHistory;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.entity.PersonalDetails;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonHistoryRepository;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonNotificationRepository;
import com.dsp.agendaPapers.eSummonNotification.repository.PersonalDetailsRepository;
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
    

////kaushik sir code  kafka and document generation
//    @KafkaListener(topics = "${kafka.topic.eSummon}", groupId = "eSummonGroup")
//    public void consume (String messageJson) {
//        try {
//
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(messageJson);
//            String id = root.get("eSummonId").asText();
//            String token = root.get("token").asText();
//        	log.info("Received Kafka event for eSummonId: {}", id);
//
//        	Long eSummonId = Long.parseLong(id);
//        	log.info("Long eSummonId: {}", eSummonId);
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
//            	UUID docUuid = generateDocument(notification, history, token);
//            	log.info("Generate document ID: {}", docUuid);
//
//                try {
//                    emailService.sendEmail(history, sessionRoman, lokSabhaNoWord, token);
//                    history.setMailStatus("SENT");
//                    history.setDocumentUuid(docUuid);
//                    history.setRetryCount(history.getRetryCount() + 1);
//                    historyRepository.save(history);
//                } catch (Exception e) {
//                    log.error("Failed to send email to member: {}. Error: {}", history.getEmailId(), e.getMessage());
//                    history.setRetryCount(history.getRetryCount() + 1);
//                    history.setDocumentUuid(docUuid);
//                    historyRepository.save(history);
//                }
//            }
//
//        } catch (Exception ex) {
//        	ex.printStackTrace();
//            log.error("Error processing Kafka message: {}", ex.getMessage());
//        }
//    }

//	private UUID generateDocument(ESummonNotification esummon, ESummonHistory history, String token) {
//
//		// ✅ Use the passed token directly (no need to extract from request)
//		if (StringUtils.isEmpty(token)) {
//			throw new RuntimeException("Missing or invalid Authorization token");
//		}
//
//		// ✅ Prepare the upload HTML request
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//		headers.setBearerAuth(token); // Use passed Bearer token
//
//		System.out.println("##### dto.getTemplateContent() - " + esummon.getTemplateContent());
//		MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
//		formParams.add("htmlContentEn", esummon.getTemplateContent());
//		formParams.add("startingPageNo", "1");
//		formParams.add("applicationId", "24");
//
//		HttpEntity<MultiValueMap<String, String>> formRequest = new HttpEntity<>(formParams, headers);
//
//		ResponseEntity<String> uploadResponse;
//		try {
//			uploadResponse = restTemplate.postForEntity("https://dspstg.sansad.in/template/upload-html-english",
//					formRequest, String.class);
//		} catch (Exception e) {
//			logger.debug ("### Document generation failed");
//			throw new RuntimeException("Failed to upload HTML content: " + e.getMessage(), e);
//		}
//
//		try {
//			ObjectMapper objectMapper = new ObjectMapper();
//			JsonNode responseJson = objectMapper.readTree(uploadResponse.getBody());
//
//			if (!responseJson.has("documentUuid") || responseJson.get("documentUuid").isNull()) {
//				throw new RuntimeException("documentUuid not found in upload-html-english response");
//			}
//
//			UUID documentUuid = UUID.fromString(responseJson.get("documentUuid").asText());
//
//			logger.debug ("### documentUuid - " + documentUuid);
//			return documentUuid;
//		} catch (Exception ex) {
//			throw new RuntimeException("Failed to process upload-html-english response", ex);
//		}
//	}
//    // kaushik sir code working code

//name replcament code by me for name
@KafkaListener(topics = "${kafka.topic.eSummon}", groupId = "eSummonGroup")
public void consume(String messageJson) {
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
            try {
                // ✅ Fetch member directly from entity
                PersonalDetails member = history.getMember();
                if (member == null) {
                    log.error("Member is null for history ID: {}", history.getId());
                    continue;
                }

                // ✅ Generate document with personalized content
                UUID docUuid = generateDocument(notification, history, member, token);
                log.info("Generated document UUID: {}", docUuid);

                // ✅ Send email and update status
                emailService.sendEmail(history, sessionRoman, lokSabhaNoWord, token);
                history.setMailStatus("SENT");
                history.setDocumentUuid(docUuid);
                history.setRetryCount(history.getRetryCount() + 1);
                historyRepository.save(history);

            } catch (Exception e) {
                log.error("Failed to process history ID: {}. Error: {}", history.getId(), e.getMessage());
                history.setRetryCount(history.getRetryCount() + 1);
                historyRepository.save(history); // Still update retry count
            }
        }

    } catch (Exception ex) {
        log.error("Error processing Kafka message: {}", ex.getMessage(), ex);
    }
}

    private UUID generateDocument(ESummonNotification esummon, ESummonHistory history, PersonalDetails member, String token) {
        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("Missing or invalid Authorization token");
        }

        // ✅ Replace placeholders like ${member.name} with actual member full name
        String templateContent = esummon.getTemplateContent();
        String personalizedHtml = replacePlaceholders(templateContent, member);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);

        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("htmlContentEn", personalizedHtml);
        formParams.add("startingPageNo", "1");
        formParams.add("applicationId", "24");

        HttpEntity<MultiValueMap<String, String>> formRequest = new HttpEntity<>(formParams, headers);

        ResponseEntity<String> uploadResponse;
        try {
            uploadResponse = restTemplate.postForEntity("https://dspstg.sansad.in/template/upload-html-english",
                    formRequest, String.class);
        } catch (Exception e) {
            logger.debug("### Document generation failed");
            throw new RuntimeException("Failed to upload HTML content: " + e.getMessage(), e);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(uploadResponse.getBody());

            if (!responseJson.has("documentUuid") || responseJson.get("documentUuid").isNull()) {
                throw new RuntimeException("documentUuid not found in upload-html-english response");
            }

            UUID documentUuid = UUID.fromString(responseJson.get("documentUuid").asText());

            // Persist document UUID
            history.setDocumentUuid(documentUuid);
            historyRepository.save(history);

            logger.debug("### documentUuid - " + documentUuid);
            return documentUuid;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to process upload-html-english response", ex);
        }
    }
    private String replacePlaceholders(String htmlTemplate, PersonalDetails member) {
        if (member == null || StringUtils.isEmpty(member.getFullName())) {
            throw new RuntimeException("Member details are missing or incomplete");
        }

        // Replace ${member.name} with actual full name
        return htmlTemplate.replace("${member.name}", member.getFullName());
    }
}
