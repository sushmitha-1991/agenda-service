//package com.dsp.agendaPapers.eSummonNotification.documentgeneration;
//
//import feign.RequestInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//@Configuration
//public class PdfFeignConfiguration {
//
//    @Bean
//    public RequestInterceptor bearerTokenRequestInterceptor() {
//        return requestTemplate -> {
//            // Fetch the current authentication token from the security context
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication != null && authentication.getCredentials() instanceof String) {
//                String currentToken = (String) authentication.getCredentials();
//                requestTemplate.header("Authorization", "Bearer " + currentToken);
//            }
//        };
//    }
//}
