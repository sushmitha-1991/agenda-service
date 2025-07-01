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

    @Autowired
    PersonalDetailsRepository personalDetailsRepository;

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);


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

                // ✅ Generate Hindi document with personalized content
                UUID docUuidHindi = generateHindiDocument(notification, history, member, token);
                log.info("Generated document UUID (Hindi): {}", docUuidHindi);

                // ✅ Send email and update status
                emailService.sendEmail(history, sessionRoman, lokSabhaNoWord, token);
                history.setMailStatus("SENT");
                history.setDocumentUuid(docUuid);
                history.setDocumentUuidHindi(docUuidHindi);
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

// new code
    private String replacePlaceholders(String htmlTemplate, Long memberId) {
        if (memberId == null) {
            logger.error("Member ID is null while replacing placeholders.");
            throw new RuntimeException("Member ID is missing");
        }

        String displayNameEn = personalDetailsRepository.getDisplayNameEn(memberId);
        String displayNameHi = personalDetailsRepository.getDisplayNameHi(memberId);

        if (displayNameEn == null) displayNameEn = "";
        if (displayNameHi == null) displayNameHi = "";

        logger.debug("Replacing placeholders: [member.name -> '{}', member.name_hindi -> '{}']", displayNameEn, displayNameHi);

        return htmlTemplate
                .replace("${member.name}", displayNameEn)
                .replace("${member.name_hindi}", displayNameHi);
    }
    // new code for english
    private UUID generateDocument(ESummonNotification esummon, ESummonHistory history, PersonalDetails member, String token) {
        if (StringUtils.isEmpty(token)) {
            logger.error("Authorization token is missing or invalid.");
            throw new RuntimeException("Missing or invalid Authorization token");
        }

        logger.info("### Starting document generation for memberId: {}", member != null ? member.getId() : "Unknown");

        String templateEn = esummon.getTemplateContent();
        logger.info("### Original English Template:\n{}", templateEn);

        // ✅ Use new method with member ID
        String personalizedHtmlEn = replacePlaceholders(templateEn, member.getId());
        logger.info("### Personalized English HTML Content:\n{}", personalizedHtmlEn);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);

        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("htmlContentEn", personalizedHtmlEn);
        formParams.add("startingPageNo", "1");
        formParams.add("applicationId", "24");

        HttpEntity<MultiValueMap<String, String>> formRequest = new HttpEntity<>(formParams, headers);

        ResponseEntity<String> uploadResponse;
        try {
            logger.info("### Sending English HTML content to document generation service...");
            uploadResponse = restTemplate.postForEntity(
                    "https://dspstg.sansad.in/template/upload-html-english", formRequest, String.class);
            logger.info("### English Document upload API called successfully.");
            logger.info("### English Upload HTML Response Body:\n{}", uploadResponse.getBody());
        } catch (Exception e) {
            logger.error("### English Document upload failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload English HTML content: " + e.getMessage(), e);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(uploadResponse.getBody());

            if (!responseJson.has("documentUuid") || responseJson.get("documentUuid").isNull()) {
                logger.info("### English documentUuid is missing in upload response.");
                throw new RuntimeException("documentUuid not found in upload-html-english response");
            }

            UUID documentUuid = UUID.fromString(responseJson.get("documentUuid").asText());
            history.setDocumentUuid(documentUuid);
            historyRepository.save(history);

            logger.info("### English Document generated successfully with UUID: {}", documentUuid);
            return documentUuid;
        } catch (Exception ex) {
            logger.error("### Failed to process upload-html-english response: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to process upload-html-english response", ex);
        }
    }

    // new code hindi
    private UUID generateHindiDocument(ESummonNotification esummon, ESummonHistory history, PersonalDetails member, String token) {
        if (StringUtils.isEmpty(token)) {
            logger.error("Authorization token is missing or invalid.");
            throw new RuntimeException("Missing or invalid Authorization token");
        }

        logger.info("### Starting Hindi document generation for memberId: {}", member != null ? member.getId() : "Unknown");

        String templateHi = esummon.getTemplateContentHindi();
        logger.info("### Original Hindi Template:\n{}", templateHi);

        // ✅ Use new method with member ID
        String personalizedHtmlHi = replacePlaceholders(templateHi, member.getId());
        logger.info("### Personalized Hindi HTML Content:\n{}", personalizedHtmlHi);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);

        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("htmlContentEn", personalizedHtmlHi); // Note: API param still uses `htmlContentEn`
        formParams.add("startingPageNo", "1");
        formParams.add("applicationId", "24");

        HttpEntity<MultiValueMap<String, String>> formRequest = new HttpEntity<>(formParams, headers);

        ResponseEntity<String> uploadResponse;
        try {
            logger.info("### Sending Hindi HTML content to document generation service...");
            uploadResponse = restTemplate.postForEntity(
                    "https://dspstg.sansad.in/template/upload-html-english", // Ensure this is correct for Hindi
                    formRequest,
                    String.class
            );
            logger.info("### Hindi Document upload API called successfully.");
            logger.info("### Hindi Upload HTML Response Body:\n{}", uploadResponse.getBody());
        } catch (Exception e) {
            logger.error("### Hindi Document upload failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload Hindi HTML content: " + e.getMessage(), e);
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(uploadResponse.getBody());

            if (!responseJson.has("documentUuid") || responseJson.get("documentUuid").isNull()) {
                logger.info("### Hindi documentUuid is missing in upload response.");
                throw new RuntimeException("documentUuid not found in upload-html-hindi response");
            }

            UUID documentUuidHindi = UUID.fromString(responseJson.get("documentUuid").asText());
            history.setDocumentUuidHindi(documentUuidHindi);
            historyRepository.save(history);

            logger.info("### Hindi Document generated successfully with UUID: {}", documentUuidHindi);
            return documentUuidHindi;
        } catch (Exception ex) {
            logger.error("### Failed to process upload-html-hindi response: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to process upload-html-hindi response", ex);
        }
    }
}
