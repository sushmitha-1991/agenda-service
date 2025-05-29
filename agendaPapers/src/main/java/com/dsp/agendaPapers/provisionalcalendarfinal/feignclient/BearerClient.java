package com.dsp.agendaPapers.provisionalcalendarfinal.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberRequest;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberResponse;

@FeignClient(name = "${feign.client.bearer.name}", url = "${feign.client.bearer.url}", configuration = BearerFeignConfiguration.class)
public interface BearerClient {
	@PostMapping("/master/serial-number")
	SerialNumberResponse getResource(@RequestBody SerialNumberRequest m);

	@DeleteMapping("/workflow/delete-save-as-draft")
	String deleteDraftEntry(@RequestParam String applicationNo);
}
