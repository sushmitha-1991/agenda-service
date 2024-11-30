package com.dsp.agendaPapers.summonNotification.dto;

import java.time.LocalDate;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummonNotificationDTO {

	private Long id;

	private String lokSabhaNo;

	private String sessionNo;
	
	private String rajyasabhaSessionNo;
	@NotEmpty
	private LocalDate summonIssuanceDate;
	@NotEmpty
	private String summonIssuanceDateHindiEn;
	@NotEmpty
	private String summonIssuanceDateHindi;
	@NotEmpty
	private String notificationNo;

	private LocalDateTime summonStartAt;
	private Long applicationId;

	private String applicationNo;
	@NotEmpty
	private String status;
	private String templateContent;
	private SummonNotificationDTO parentNotification;
	private ApplicationInstanceDTO applicationInstanceDTO;
	private String currentStatus;
	private String publishedDocumentUrl;
	private Long templateId;
	private Long templateHindiId;
	private String templateContentHindi;
	private String TemplateType;
	private Long refId;
}
