/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.agendaPapers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveAsDraftDTO {

	private String lokSabhaSession;
	private String lokSabhaSessionNumber;
	private String rajyaSabhaSession;
	private String applicationNo;
	private String currentStatus;
	private String taskId;
	private String processInstanceId;
	private String formId;
	private String other;
	private Long applicationId;
}
