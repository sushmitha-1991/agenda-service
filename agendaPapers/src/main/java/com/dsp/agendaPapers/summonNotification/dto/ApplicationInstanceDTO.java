package com.dsp.agendaPapers.summonNotification.dto;

//SummonNotification Microservice - DTO for sending data to ApplicationInstance


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationInstanceDTO {
 //private String house;
 private Long applicationId; // Reference to Application ID
 private String applicationNo;
 private String currentStatus;
 private String publishedDocumentUrl;
}
