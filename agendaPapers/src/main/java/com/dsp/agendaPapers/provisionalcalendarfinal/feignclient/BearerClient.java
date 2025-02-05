package com.dsp.agendaPapers.provisionalcalendarfinal.feignclient;

import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberRequest;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;

//@FeignClient(name = "bearerClient",url="https://dsptest.sansad.in" ,configuration = BearerFeignConfiguration.class)
////@FeignClient(name = "bearerClient",url="http://10.247.128.16" ,configuration = BearerFeignConfiguration.class)


@FeignClient(
        name = "${feign.client.bearer.name}",
        url = "${feign.client.bearer.url}",
        configuration = BearerFeignConfiguration.class
)
public interface BearerClient {
    @PostMapping("/master/serial-number")
    SerialNumberResponse getResource(@RequestBody SerialNumberRequest m);
    //workFlow delete 
    @DeleteMapping("/workflow/delete-save-as-draft")
    String deleteDraftEntry(@RequestParam String applicationNo);
}

// when we need to define into the Application.properties file

//@FeignClient(
//        name = "${feign.client.bearer.name}",
//        url = "${feign.client.bearer.url}",
//        configuration = BearerFeignConfiguration.class
//)