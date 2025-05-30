package com.dsp.agendaPapers.eSummonNotification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESummonMemberDetailsDTO {
    private Long memberId;
    private String fullName;
    private UUID profilePicture;
    private String partyName;
    private String partyAbbr;
    private String constituencyName;
    private String constituencyAbbr;
    private String stateName;
    private String mailStatus;
    private String documentUuid;
    private UUID documentUuidHindi;
}
