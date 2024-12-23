package com.dsp.agendaPapers.prorogationnotice.Dto;

import java.time.LocalDate;


import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProrogationNoticeDTO {

    private Long id;
    private String lokSabhaNo;
    private String lokSabhaSessionNo;
    private String rajyaSabhaSessionNo;
    private LocalDate issuanceDate;
    private String issuanceDateSakaEra;
    private String issuanceDateSakaEraHindi;
    private String notificationNo;
    private String refNo;
    private String templateContent;
    private String templateContentHindi;
    private Boolean draftFlag;
    private String status;
    private Long applicationId;
    private String applicationNo;
    private String currentStatus;
	private String publishedDocumentUrl;
	private String templateType;
	private Long templateId;
	private Long templateHindiId;
	private ApplicationInstanceDTO applicationInstanceDTO;


    // Getters and Setters
}
