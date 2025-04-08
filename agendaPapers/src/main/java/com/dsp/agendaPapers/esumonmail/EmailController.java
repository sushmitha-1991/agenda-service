package com.dsp.agendaPapers.esumonmail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/agendapapers/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String sendEmailMultipart(
            @RequestParam("file") MultipartFile file,
            @RequestParam("emailfrom") String emailFrom,
            @RequestParam("emailsub") String emailSub,
            @RequestParam("emailto") String emailTo,
            @RequestParam("emailbody") String emailBody
    ) throws IOException {

        // Save the uploaded file to a temp location
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);

        // Prepare the DTO
        EmailRequestDTO request = new EmailRequestDTO();
        request.setFilePath(tempFile.getAbsolutePath());
        request.setEmailFrom(emailFrom);
        request.setEmailSub(emailSub);
        request.setEmailTo(emailTo);
        request.setEmailBody(emailBody);

        // Call the service
        return emailService.sendEmail(request);
    }
}
