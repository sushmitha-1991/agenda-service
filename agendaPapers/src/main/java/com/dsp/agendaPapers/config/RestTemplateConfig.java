package com.dsp.agendaPapers.config;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

        // Add interceptor to attach JWT token
        ClientHttpRequestInterceptor interceptor = (req, body, execution) -> {
            String jwtToken = request.getHeader("Authorization");
            if (jwtToken != null && !jwtToken.isEmpty()) {
                req.getHeaders().add("Authorization", jwtToken);
            }
            return execution.execute(req, body);
        };

        // Set the interceptor to add the Authorization header with JWT token
        restTemplate.setInterceptors(Collections.singletonList(interceptor));
        return restTemplate;
    }

    // Factory to configure timeouts for RestTemplate requests
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        // Configure timeout settings
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3, TimeUnit.SECONDS)  // Connection timeout (3 seconds)
                .setResponseTimeout(3, TimeUnit.SECONDS)  // Response timeout (3 seconds)
                .build();

        // Create the HttpClient with the request configuration
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        // Create and return the request factory with the custom HttpClient
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
