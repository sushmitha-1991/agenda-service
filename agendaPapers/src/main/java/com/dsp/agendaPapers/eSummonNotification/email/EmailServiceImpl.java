package com.dsp.agendaPapers.eSummonNotification.email;

import com.dsp.agendaPapers.eSummonNotification.entity.ESummonHistory;
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
    public void sendEmail(ESummonHistory history, String sessionRoman, String lokSabhaNoWord,String token) throws Exception {
        String emailApiUrl = "http://10.246.55.102/emailapi/SendEmail";

        System.out.println("### sending the email started ");
        // Prepare headers with Authorization
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.set("Authorization", "Bearer " + token); // <- use passed token

        // Subject & Body
        String subject = String.format("Summons for IV Session of %s Lok Sabha", sessionRoman, lokSabhaNoWord);
        String body = String.format("Summons for IV Session of %s Lok Sabha is attached for your information", sessionRoman, lokSabhaNoWord);

        // Build request body
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("emailfrom", "noreply@sansad.in");
        formData.add("emailto", history.getEmailId());
        formData.add("emailsub", subject);
        formData.add("emailbody", body);

        // Attach file using UUID
        if (history.getDocumentUuid() != null) {
            ByteArrayResource fileResource = downloadFileFromUUID(history.getDocumentUuid());

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

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, headers);

        log.info("Sending email to: {}, subject: {}", history.getEmailId(), subject);
        ResponseEntity<String> response = restTemplate.postForEntity(emailApiUrl, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Failed to send email to " + history.getEmailId() + ": " + response.getBody());
        }

        log.info("Email successfully sent to {} with attachment", history.getEmailId());
    }

    private ByteArrayResource downloadFileFromUUID(UUID uuid) {
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
                        return uuid + ".pdf";
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

}