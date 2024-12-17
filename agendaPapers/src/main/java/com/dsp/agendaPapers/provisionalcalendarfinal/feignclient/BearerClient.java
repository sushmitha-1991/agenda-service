package com.dsp.agendaPapers.provisionalcalendarfinal.feignclient;

import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberRequest;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "bearerClient",url="https://dsptest.sansad.in" ,configuration = BearerFeignConfiguration.class)
public interface BearerClient {
    @PostMapping("/master/serial-number")
    SerialNumberResponse getResource(@RequestBody SerialNumberRequest m);
}