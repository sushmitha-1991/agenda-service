package com.dsp.agendaPapers.eSummonNotification.dto;

import java.time.LocalDate;

import java.time.LocalDateTime;

import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;
import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ESummonNotificationDTO {

private Long id;
	
    private String lokSabhaNo;
   
    private String sessionNo;

    private LocalDate summonIssuanceDate;
   
    private String summonIssuanceDateHindiEn;
   
    private String summonIssuanceDateHindi;

    private LocalDateTime summonStartAt;
   private Long applicationId;
    private String applicationNo;
   
    private String status;
    private SummonNotificationDTO parentNotification;
   
    private String templateContent;
    private ApplicationInstanceDTO applicationInstanceDTO;
    private String currentStatus;
    private String publishedDocumentUrl;
   private Long templateId;
    private Long refId;
}


