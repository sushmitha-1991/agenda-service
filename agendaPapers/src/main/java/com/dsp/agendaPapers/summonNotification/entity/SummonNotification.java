package com.dsp.agendaPapers.summonNotification.entity;

import java.time.LocalDate;

import java.time.LocalDateTime;

import com.dsp.agendaPapers.entity.Auditable;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "summon_notifications", schema = "agenda_papers")
public class SummonNotification extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "summon_notifications_seq")
	@SequenceGenerator(name = "summon_notifications_seq", sequenceName = "summon_notifications_seq", allocationSize = 1)
	private Long id;

	@Column(name = "lok_sabha_no", length = 100)
	private String lokSabhaNo;

	@Column(name = "session_no", length = 100)
	private String sessionNo;
	
	@Column(name = "rajyasabha_SessionNo", length = 100)
	private String rajyasabhaSessionNo;

	@Column(name = "summon_issuance_date", nullable = false)
	private LocalDate summonIssuanceDate;

	@Column(name = "summon_issuance_date_hindi_en", nullable = false, length = 100)
	private String summonIssuanceDateHindiEn;

	@Column(name = "summon_issuance_date_hindi", nullable = false, length = 100)
	private String summonIssuanceDateHindi;

	@Column(name = "notification_no", nullable = false, length = 100)
	private String notificationNo;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "summon_start_at")
	private LocalDateTime summonStartAt;

	@Column(name = "application_no", length = 50)
	private String applicationNo;

	@Column(name = "status", nullable = false, length = 50)
	private String status;

	@Column(name = "template_content", columnDefinition = "TEXT")
	private String templateContent;

	@Column(name = "template_content_Hindi", columnDefinition = "TEXT")
	private String templateContentHindi;
	
	@Column(name = "template_type")
	private String templateType;

	@OneToOne
	@JoinColumn(name = "parent_id")
	private SummonNotification parentNotification;

}