package com.dsp.agendaPapers.provisionalcalendarfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionalCalendarGenerateDTO {

	private Long id;
	private String content;
	private String status;
	private Long provisionalCalendarInputId;
	private Long applicationId;

}
