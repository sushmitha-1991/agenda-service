package com.dsp.agendaPapers.eSummonHistory.dto;



import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private String emailId;



    // for the fetching the data
//    private ESummonNotificationDTO eSummon;

    // Optional: mapping to ESummonNotification entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "e_summon_id", insertable = false, updatable = false)
    private ESummonNotification eSummon;
}

