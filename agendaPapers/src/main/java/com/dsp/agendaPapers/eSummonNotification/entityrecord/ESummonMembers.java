package com.dsp.agendaPapers.eSummonNotification.entityrecord;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "e_summon_members", schema = "agenda_papers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ESummonMembersId.class)
public class ESummonMembers {

    @Id
    @Column(name = "e_summon_id")
    private Long eSummonId;

    @Id
    @Column(name = "member_id")
    private Long memberId;
}
