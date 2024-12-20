package com.dsp.agendaPapers.applicationPapers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplatesDTO {
	
	private long id;
    private Long applicationId;
    private String templateName;
    private String abbr;
    private String content;
    private String language;

}
