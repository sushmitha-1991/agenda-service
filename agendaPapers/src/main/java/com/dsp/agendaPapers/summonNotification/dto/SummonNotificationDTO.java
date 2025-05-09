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

	private LocalDate summonIssuanceDate;
	
	private String summonIssuanceDateHindiEn;
	
	private String summonIssuanceDateHindi;

	private String notificationNo;

	private LocalDateTime summonStartAt;
	private Long applicationId;
	private Boolean draftStatus = false;
	private Boolean customTemplate;
	private String applicationNo;
	
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
        
        private LocalDate presidentSigningDate;

}
