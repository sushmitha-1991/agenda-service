///**
// * 
// * @author Mrinal Lakhanpal
// * @version 1.0
// */
///**
// * 
// * @author Mrinal Lakhanpal
// * @version 1.0
// */
//package com.dsp.agendaPapers.summonNotification.entity;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//import com.dsp.agendaPapers.entity.ApplicationInstance;
//import com.fasterxml.jackson.annotation.JsonFormat;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.experimental.SuperBuilder;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@SuperBuilder
//@Entity
//@Table(name = "summon_notifications_test", schema = "agenda_papers")
//public class SummonNotificationTest extends ApplicationInstance {
//
//	@Column(name = "lok_sabha_no", nullable = false, length = 100)
//	private String lokSabhaNo;
//
//	@Column(name = "session_no", nullable = false, length = 100)
//	private String sessionNo;
//
//	@Column(name = "summon_issuance_date", nullable = false)
//	private LocalDate summonIssuanceDate;
//
//	@Column(name = "summon_issuance_date_hindi_en", nullable = false, length = 100)
//	private String summonIssuanceDateHindiEn;
//
//	@Column(name = "summon_issuance_date_hindi", nullable = false, length = 100)
//	private String summonIssuanceDateHindi;
//
//	@Column(name = "notification_no", nullable = false, length = 100)
//	private String notificationNo;
//
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
//	@Column(name = "summon_start_at")
//	private LocalDateTime summonStartAt;
//
//	@Column(name = "application_no", length = 50)
//	private String applicationNo;
//
//	@Column(name = "status", nullable = false, length = 50)
//	private String status;
//
//	@Column(name = "template_content", columnDefinition = "TEXT")
//	private String templateContent;
//
//	@Column(name = "template_content_Hindi", columnDefinition = "TEXT")
//	private String templateContentHindi;
//
//	@OneToOne
//	@JoinColumn(name = "parent_id")
//	private SummonNotificationTest parentNotification;
//
//}