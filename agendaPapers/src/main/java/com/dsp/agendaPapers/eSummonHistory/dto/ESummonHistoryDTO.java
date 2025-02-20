package com.dsp.agendaPapers.eSummonHistory.dto;



import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ESummonHistoryDTO {
    private Long id;

    @JsonProperty("eSummonId") // Ensure JSON field maps to Java field
    private Long eSummonId;

    @JsonProperty("memberId")
    private Long memberId;

    private UUID documentUuid;
    private LocalDateTime mailSentAt;
    private String mailStatus;
    private LocalDateTime lastRetryDatetime;
    private int retryCount;
    private String remarks;

    // for the fetching the data
    private ESummonNotificationDTO eSummon;
}

