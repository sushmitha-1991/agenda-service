package com.dsp.agendaPapers.mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import com.dsp.agendaPapers.eSummonHistory.entity.PersonalDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;

import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;

import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
public class ESummonNotificationMapper {

	// Convert entity to DTO using the builder pattern with null checks
	public static ESummonNotificationDTO toDTO(ESummonNotification eSummonNotification) {
		if (eSummonNotification == null) {
			return null;
		}

		// Initialize ObjectMapper
		ObjectMapper mapper = new ObjectMapper();

		// Initialize memberIdsList
		List<ESummonNotificationDTO.MemberIdDTO> memberIdsList = Collections.emptyList();

		// Deserialize memberIds JSON string to List<MemberIdDTO>
		if (eSummonNotification.getMemberIds() != null) {
			try {
				memberIdsList = mapper.readValue(
						eSummonNotification.getMemberIds(),
						new TypeReference<List<ESummonNotificationDTO.MemberIdDTO>>() {}
				);
			} catch (JsonProcessingException e) {
				// Log the error
				// Handle the exception as needed, e.g., rethrow or set memberIdsList to an empty list
			}
		}

		return ESummonNotificationDTO.builder()
				.id(eSummonNotification.getId())
				.lokSabhaNo(eSummonNotification.getLokSabhaNo())
				.sessionNo(eSummonNotification.getSessionNo())
				.rajyasabhaSessionNo(eSummonNotification.getRajyasabhaSessionNo())
				.esummonIssuanceDate(eSummonNotification.getEsummonIssuanceDate())
				.esummonIssuanceDateHindi(eSummonNotification.getEsummonIssuanceDateHindi())
				.esummonIssuanceDateHindiEn(eSummonNotification.getEsummonIssuanceDateHindiEn())
				.applicationNo(eSummonNotification.getApplicationNo())
				.status(eSummonNotification.getStatus())
				.templateContent(eSummonNotification.getTemplateContent())
				.templateContentHindi(eSummonNotification.getTemplateContentHindi())
				.templateType(eSummonNotification.getTemplateType())
				.draftStatus(eSummonNotification.getDraftStatus())
				.customTemplate(eSummonNotification.getCustomTemplate())
				.esummonStartAt(eSummonNotification.getEsummonStartAt())
				.memberIds(memberIdsList)
				// Uncomment if recursive mapping is needed
				// .parentNotification(toDTO(eSummonNotification.getParentNotification()))
				.build();
	}
	    // Convert DTO to entity
	    public static ESummonNotification toEntity(ESummonNotificationDTO dto) {
	        if (dto == null) {
	            return null;
	        }

	        return ESummonNotification.builder()
	                .id(dto.getId())
	                .lokSabhaNo(dto.getLokSabhaNo())
	                .sessionNo(dto.getSessionNo())
	                .rajyasabhaSessionNo(dto.getRajyasabhaSessionNo())
	                .esummonIssuanceDate(dto.getEsummonIssuanceDate())
	                .esummonIssuanceDateHindi(dto.getEsummonIssuanceDateHindi())
	                .esummonIssuanceDateHindiEn(dto.getEsummonIssuanceDateHindiEn())
	                .applicationNo(dto.getApplicationNo())
	                .status(dto.getStatus())
	                .templateContent(dto.getTemplateContent())
	                .templateContentHindi(dto.getTemplateContentHindi())
	                .templateType(dto.getTemplateType())
	                .draftStatus(dto.getDraftStatus())
	                .customTemplate(dto.getCustomTemplate())
	                
	                .esummonStartAt(dto.getEsummonStartAt())
					// member handle by the samar

	                // Uncomment if recursive mapping is needed
	                // .parentNotification(toEntity(dto.getParentNotification()))
	                .build();
	    }
}
