package com.dsp.agendaPapers.eSummonNotification.entity;

import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.entity.Auditable;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "e_summon_history", schema = "agenda_papers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ESummonHistory extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "e_summon_history_seq")
    @SequenceGenerator(
            name = "e_summon_history_seq",
            sequenceName = "agenda_papers.e_summon_history_seq",  // Ensure that the sequence name is correct
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "e_summon_id", nullable = false)
    private ESummonNotification eSummon;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private PersonalDetails member;

    @Column(name = "document_uuid", nullable = false)
    private UUID documentUuid;

    @Column(name = "mail_sent_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime mailSentAt;

    @Column(name = "mail_status", nullable = false, length = 50)
//    @Builder.Default
//    private String mailStatus = "PENDING";
    private String mailStatus;

    @Column(name = "last_retry_datetime")
    private LocalDateTime lastRetryDatetime;

    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "email_id", nullable = false, length = 255)
    private String emailId;


}
