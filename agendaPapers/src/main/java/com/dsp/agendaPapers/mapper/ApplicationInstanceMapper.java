package com.dsp.agendaPapers.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dsp.agendaPapers.dto.ApplicationInstanceResponseDTO;
import com.dsp.agendaPapers.entity.ApplicationInstance;

@Component
public class ApplicationInstanceMapper {

	// Convert ApplicationInstance to ApplicationInstanceResponseDTO
	public ApplicationInstanceResponseDTO toDTO(ApplicationInstance applicationInstance) {
		if (applicationInstance == null) {
			return null;
		}

		return ApplicationInstanceResponseDTO.builder().id(applicationInstance.getId())
				.applicationId(applicationInstance.getApplicationId())
				.applicationNo(applicationInstance.getApplicationNo()).refId(applicationInstance.getRefId())
				.currentStatus(applicationInstance.getCurrentStatus())
				.publishedDocumentUrl(applicationInstance.getPublishedDocumentUrl())
				.processInstanceId(applicationInstance.getProcessInstanceId())
				.isPublished(applicationInstance.getIsPublished())
				.lokSabhaSession(applicationInstance.getLokSabhaSession())
				.lokSabhaSessionNumber(applicationInstance.getLokSabhaSessionNumber())
				.rajyaSabhaSession(applicationInstance.getRajyaSabhaSession()).taskId(applicationInstance.getTaskId())
				.otherInfo(applicationInstance.getOtherInfo()).draft(applicationInstance.getDraft()).build();
	}

	// Convert List of ApplicationInstance to List of ApplicationInstanceResponseDTO
	public List<ApplicationInstanceResponseDTO> toDTOs(List<ApplicationInstance> applicationInstances) {
		if (applicationInstances == null || applicationInstances.isEmpty()) {
			return null;
		}

		return applicationInstances.stream().map(this::toDTO).collect(Collectors.toList());
	}
}
