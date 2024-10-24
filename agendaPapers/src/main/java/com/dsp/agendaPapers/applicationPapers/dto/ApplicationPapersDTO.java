package com.dsp.agendaPapers.applicationPapers.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationPapersDTO {
	
	private Long id;
	
	private Long applicationInstanceId;
	
	private Long applicationId;
	
	private Long templateId;
	
    private String templateContent;

}
