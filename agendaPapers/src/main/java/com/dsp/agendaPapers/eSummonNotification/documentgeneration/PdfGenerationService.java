//package com.dsp.agendaPapers.eSummonNotification.documentgeneration;
//
//import feign.FeignException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PdfGenerationService {
//
//    @Autowired
//    private PdfClient pdfClient;
//
//    public String generatePdf(String htmlContent, String fileName) {
//        try {
//            return pdfClient.generatePdf(htmlContent, fileName);
//        } catch (FeignException e) {
//            // Handle error (e.g., log the error, return custom message)
//            return "Error occurred: " + e.getMessage();
//        }
//    }
//}
