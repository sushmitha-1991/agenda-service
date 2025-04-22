package com.dsp.agendaPapers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
//@EnableFeignClients(basePackages = "com.dsp.agendaPapers.provisionalcalendarfinal.feignclient")

@EnableFeignClients(basePackages = {
		"com.dsp.agendaPapers.provisionalcalendarfinal.feignclient",
		"com.dsp.agendaPapers.eSummonNotification.documentgeneration"
})
public class AgendaPapersApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgendaPapersApplication.class, args);
	}

}
