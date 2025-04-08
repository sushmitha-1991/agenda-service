package com.dsp.agendaPapers.esumonmail;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendEmail(EmailRequestDTO request)
    {
        String url = "http://10.246.55.102/emailapi/SendEmail";

        File file = new File(request.getFilePath());
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + request.getFilePath());
        }

        FileSystemResource fileResource = new FileSystemResource(file);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);
        body.add("emailfrom", request.getEmailFrom());
        body.add("emailsub", request.getEmailSub());
        body.add("emailto", request.getEmailTo());
        body.add("emailbody", request.getEmailBody());
        System.out.println("url  "+url );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        System.out.println("###");
        System.out.println(entity);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return response.getBody();

        }
         catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            throw  new RuntimeException("error in api call"+e);
        }
//return  null;
    }

}
