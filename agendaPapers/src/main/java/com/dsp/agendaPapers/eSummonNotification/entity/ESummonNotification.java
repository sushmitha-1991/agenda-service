package com.dsp.agendaPapers.eSummonNotification.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dsp.agendaPapers.entity.Auditable;
import com.dsp.agendaPapers.summonNotification.entity.SummonNotification;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "e_summon", schema = "agenda_papers")

public class ESummonNotification extends Auditable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eSummon_notifications_seq")
	@SequenceGenerator(name = "eSummon_notifications_seq", sequenceName = "eSummon_notifications_seq", allocationSize = 1)
	private Long id;
	
	@Column(name = "lok_sabha_no", nullable = false, length = 100)
    private String lokSabhaNo;

    @Column(name = "session_no", nullable = false, length = 100)
    private String sessionNo;

    @Column(name = "summon_issuance_date", nullable = false)
    private LocalDate summonIssuanceDate;

    @Column(name = "summon_issuance_date_hindi_en", nullable = false, length = 100)
    private String summonIssuanceDateHindiEn;
    
    @Column(name = "summon_issuance_date_hindi", nullable = false, length = 100)
    private String summonIssuanceDateHindi;

    @Column(name = "application_no", length = 50)
    private String applicationNo;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "template_content", columnDefinition = "TEXT")
    private String templateContent;
    
    @Column(name = "template_content_Hindi", columnDefinition = "TEXT")
    private String templateContentHindi;
    
    @OneToOne
    @JoinColumn(name = "parent_id")
    private ESummonNotification parentNotification;

}
