package com.dsp.agendaPapers.mapper;

import org.springframework.stereotype.Component;

import com.dsp.agendaPapers.dto.SaveAsDraftDTO;
import com.dsp.agendaPapers.entity.ApplicationInstance;
import com.dsp.agendaPapers.entity.ApplicationInstanceDraftStatus;

@Component
public class SaveAsDraftMapper {

	public ApplicationInstance toApplicationInstance(SaveAsDraftDTO dto) {
		return ApplicationInstance.builder().applicationNo(dto.getApplicationNo()).currentStatus(dto.getCurrentStatus())
				.lokSabhaSession(dto.getLokSabhaSession()).lokSabhaSessionNumber(dto.getLokSabhaSessionNumber())
				.rajyaSabhaSession(dto.getRajyaSabhaSession()).processInstanceId(dto.getProcessInstanceId())
				.taskId(dto.getTaskId()).applicationId(dto.getApplicationId()).draft(true).build();
	}

	public ApplicationInstanceDraftStatus toDraftStatus(SaveAsDraftDTO dto, Long applicationInstanceId) {
		return ApplicationInstanceDraftStatus.builder().applicationInstanceId(applicationInstanceId)
				.formId(dto.getFormId()).taskId(dto.getTaskId()).other(dto.getOther()).build();
	}
}
