package com.dsp.agendaPapers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Autowired
	private AuthorizationHeaderInterceptor authorizationHeaderInterceptor;

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("### inside RestTemplate - " + restTemplate);
		// Add the interceptor to the RestTemplate
		restTemplate.getInterceptors().add(authorizationHeaderInterceptor);
		return restTemplate;
	}

}