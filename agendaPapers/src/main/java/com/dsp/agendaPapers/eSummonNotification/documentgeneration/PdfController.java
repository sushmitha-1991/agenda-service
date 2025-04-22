package com.dsp.agendaPapers.eSummonNotification.documentgeneration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendapapers")
public class PdfController {

    @Autowired
    private PdfGenerationService pdfService;

    @PostMapping("/generate")
    public String generatePdf(
            @RequestParam String html,
            @RequestParam String fileName
    ) {
        // Call the service to generate the PDF and return the response
        return pdfService.generatePdf(html, fileName);
    }
}
