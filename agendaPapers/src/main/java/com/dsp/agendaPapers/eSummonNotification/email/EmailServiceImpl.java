package com.dsp.agendaPapers.eSummonNotification.email;

import com.dsp.agendaPapers.eSummonNotification.entity.ESummonHistory;
import com.dsp.agendaPapers.eSummonNotification.entity.PersonalDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private HttpServletRequest httpRequest;

    @Override
    public void sendEmail(ESummonHistory history, String unused1, String unused2,String token) throws Exception {
        String emailApiUrl = "http://10.246.55.102/emailapi/SendEmail";

        System.out.println("### sending the email started ");
        // Prepare headers with Authorization
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.set("Authorization", "Bearer " + token); // <- use passed token

        // Get member name
        PersonalDetails member = history.getMember();
        String fullName = member != null && member.getFullName() != null ? member.getFullName() : "Member";


        String sessionRoman = "";
        String lokSabhaNoWord = "";

        // Call session API
        try {
            RestTemplate tempRestTemplate = new RestTemplate();
            ResponseEntity<String> response = tempRestTemplate.getForEntity("https://dspstg.sansad.in/master/session/current", String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody()).path("data");

                // Use sessionNumber directly (already Roman like "IV")
                sessionRoman = root.path("sessionNumber").asText().trim();

                // Convert Lok Sabha number (e.g., "18" to "Eighteenth")
                String lokSabhaStr = root.path("lokSabhaNumber").asText().trim();
                int lokSabhaNo = NumberConverterUtil.parseIntSafe(lokSabhaStr);
                lokSabhaNoWord = NumberConverterUtil.toOrdinalWord(lokSabhaNo).trim();
            }
        } catch (Exception ex) {
            log.warn("Failed to fetch session info from API. Error: {}", ex.getMessage());
        }

        // Final fallback (make sure values are never empty, but not repeated)
        if (sessionRoman.isBlank()) sessionRoman = "IV";
        if (lokSabhaNoWord.isBlank()) lokSabhaNoWord = "Eighteenth";

// 🔁 Final fallback if API failed or returned invalid data
        if (sessionRoman == null || sessionRoman.isBlank()) {
            log.warn("sessionRoman is missing. Falling back to default.");
            sessionRoman = "Session"; // or you can leave blank ""
        }
        if (lokSabhaNoWord == null || lokSabhaNoWord.isBlank()) {
            log.warn("lokSabhaNoWord is missing. Falling back to default.");
            lokSabhaNoWord = "Lok Sabha"; // or you can leave blank ""
        }

        // Prepare subject and body
        String subject = String.format("Summons for %s Session of %s Lok Sabha", sessionRoman, lokSabhaNoWord);

        String body = String.format(
                "Dear Shri %s,<br><br>" +
                        "The Summons for %s Session of %s Lok Sabha is attached for your information.<br><br>" +
                        "Yours Sincerely,<br>" +
                        "Secretary General<br><br>" +
                        "E-mail Attachments: Two .pdf files for English and Hindi Summon",
                fullName, sessionRoman, lokSabhaNoWord
        );

        // Build request body
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("emailfrom", "noreply@sansad.in");
        formData.add("emailto", history.getEmailId());
        formData.add("emailsub", subject);
        formData.add("emailbody", body);

        // ✅ Use applicationNo as filename base
        String applicationNo = history.getApplicationNo() != null ? history.getApplicationNo() : "EsumonDocument";

        // Attach file using UUID
        if (history.getDocumentUuid() != null) {
            ByteArrayResource fileResource = downloadFileFromUUID(history.getDocumentUuid(),applicationNo);

            System.out.println("### fileResource -  " + fileResource);


            if (fileResource != null) {
                formData.add("file", fileResource);
                System.out.println("### inside if ####");
                log.info("Attached file with UUID: {}", history.getDocumentUuid());
            } else {
                log.info("Attachment not found for UUID: {}", history.getDocumentUuid());
            }
        }
        else {
            log.info("No UUID found in history to attach a file.");
        }

        // Attach Hindi file
        if (history.getDocumentUuidHindi() != null) {
            ByteArrayResource hindiFileResource = downloadHindiFileFromUUID(history.getDocumentUuidHindi(),applicationNo);

            System.out.println("### hindiFileResource - " + hindiFileResource);

            if (hindiFileResource != null) {
                formData.add("file", hindiFileResource); // Attach Hindi file (same param name — most APIs accept multiple)
                System.out.println("### Attached Hindi file ###");
                log.info("Attached Hindi file with UUID: {}", history.getDocumentUuidHindi());
            } else {
                log.info("Hindi attachment not found for UUID: {}", history.getDocumentUuidHindi());
            }
        } else {
            log.info("No Hindi UUID found in history to attach a file.");
        }

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, headers);

        log.info("Sending email to: {}, subject: {}", history.getEmailId(), subject);
        ResponseEntity<String> response = restTemplate.postForEntity(emailApiUrl, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Failed to send email to " + history.getEmailId() + ": " + response.getBody());
        }

        log.info("Email successfully sent to {} with attachment", history.getEmailId());
    }
// for english
    private ByteArrayResource downloadFileFromUUID(UUID uuid, String applicationNo) {
        try {
            String downloadUrl = "https://dspstg.sansad.in/dms/openkm/dsp1/downloads/" + uuid;

            System.out.println("### downloadFileFromUUID called");
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    downloadUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    byte[].class
            );
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return new ByteArrayResource(response.getBody()) {
                    @Override
                    public String getFilename() {

//                        return uuid + ".pdf";
                        return applicationNo + "_English.pdf";
                    }
                };
            } else {
                log.info("Failed to download file for UUID: {}, Status: {}", uuid, response.getStatusCode());
                System.out.println("#### Failed to download file for UUID");
            }
        } catch (Exception e) {
            System.out.println("#### Error downloading file for UUID" + uuid);
            log.info("Error downloading file for UUID: " + uuid, e);
        }

        return null;
    }
        private String getTokenFromRequest() {
        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    // for the hindi
    private ByteArrayResource downloadHindiFileFromUUID(UUID hindiUuid, String applicationNo) {
        try {
            String downloadUrl = "https://dspstg.sansad.in/dms/openkm/dsp1/downloads/" + hindiUuid;

            System.out.println("### downloadHindiFileFromUUID called");
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    downloadUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    byte[].class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return new ByteArrayResource(response.getBody()) {
                    @Override
                    public String getFilename() {
//                        return hindiUuid + ".pdf";
                        return applicationNo + "_Hindi.pdf";
                    }
                };
            } else {
                log.info("Failed to download Hindi file for UUID: {}, Status: {}", hindiUuid, response.getStatusCode());
                System.out.println("#### Failed to download Hindi file for UUID");
            }
        } catch (Exception e) {
            System.out.println("#### Error downloading Hindi file for UUID " + hindiUuid);
            log.info("Error downloading Hindi file for UUID: " + hindiUuid, e);
        }

        return null;
    }


}