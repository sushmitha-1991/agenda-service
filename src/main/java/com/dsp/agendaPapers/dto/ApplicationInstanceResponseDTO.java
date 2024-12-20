/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.agendaPapers.dto;

import java.util.Map;

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
public class ApplicationInstanceResponseDTO {
	private Long id;
	private Long applicationId;
	private String applicationNo;
	private Long refId;
	private String currentStatus;
	private String publishedDocumentUrl;
	private String processInstanceId;
	private Boolean isPublished;
	private String lokSabhaSession;
	private String lokSabhaSessionNumber;
	private String rajyaSabhaSession;
	private String taskId;
	private Map<String, Object> otherInfo;
	private Boolean draft;

}
