package com.dsp.agendaPapers.eSummonNotification.dto;
import lombok.Data;
import java.util.List;

@Data
public class SendESummonRequest {
    private Long id;              // eSummonNotification id
    private List<Long> members;   // List of PersonalDetails ids
}
