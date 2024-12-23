package com.dsp.agendaPapers.summonNotification.dto;

import java.time.LocalDate;

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
	private Long id;
	private Long applicationId; // Reference to Application ID
	private String applicationNo;
	private String currentStatus;
	private String publishedDocumentUrl;
	private Long refId;
	 private LocalDate documentDate;
	//private LocalDate documentDate;
}
