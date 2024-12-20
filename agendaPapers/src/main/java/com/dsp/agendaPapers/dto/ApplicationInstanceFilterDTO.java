package com.dsp.agendaPapers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationInstanceFilterDTO {
	private String lokSabhaSession;
	private String lokSabhaSessionNumber;
	private String rajyaSabhaSession;
	private Long applicationId;
}
