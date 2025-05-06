//package com.dsp.agendaPapers.eSummonNotification.documentgeneration;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@FeignClient(
//        name = "${feign.client.pdf.name}",
//        url = "${feign.client.pdf.url}",
//        configuration = PdfFeignConfiguration.class
//)
//public interface PdfClient {
//
//    @PostMapping(value = "/generatePdfFromHtml", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    String generatePdf(@RequestParam("html") String html, @RequestParam("fileName") String fileName);
//}
