package com.dsp.agendaPapers.eSummonNotification.entityrecord;

import java.io.Serializable;
import lombok.Data;

@Data
public class ESummonMembersId implements Serializable {
    private Long eSummonId;
    private Long memberId;
}
