package com.dsp.agendaPapers.eSummonNotification.email;

import com.dsp.agendaPapers.eSummonNotification.entity.ESummonHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendEmail(ESummonHistory history, String sessionRoman, String lokSabhaNoWord) throws Exception {
        String emailApiUrl = "http://10.246.55.102/emailapi/SendEmail";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 📌 Dynamic Subject & Body
        String subject = String.format("Summons for %s Session of %s Lok Sabha", sessionRoman, lokSabhaNoWord);
        String body = String.format("Summons for %s Session of %s Lok Sabha is attached for your information", sessionRoman, lokSabhaNoWord);

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("emailfrom", "noreply@sansad.in");
        formData.add("emailto", history.getEmailId());
        formData.add("emailsub", subject);
        formData.add("emailbody", body);
        formData.add("uuid", history.getDocumentUuid().toString());

        //logger
        log.info("[EmailService] Sending email to: {} | Subject: {}", history.getEmailId(), subject);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(emailApiUrl, request, String.class);

        // logger
        log.info("[EmailService] Email API Response Status: {}, Body: {}", response.getStatusCode(), response.getBody());

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Failed to send email to " + history.getEmailId() + ": " + response.getBody());
        }

        log.info("Email successfully sent to {}", history.getEmailId());
    }

    // 🔽 Method to download file from DMS using UUID
    private ByteArrayResource downloadFileFromUUID(UUID uuid) {
        try {
            String downloadUrl = "https://dspstg.sansad.in/dms/openkm/dsp1/downloads/" + uuid;

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
                        return uuid + ".pdf";
                    }
                };
            } else {
                log.error("Failed to download file for UUID: {}, status: {}", uuid, response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error downloading file for UUID: " + uuid, e);
        }

        return null;
    }
}