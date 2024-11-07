package com.dsp.agendaPapers.mapper;

import org.springframework.stereotype.Component;

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

		return ESummonNotificationDTO.builder().id(eSummonNotification.getId())
				.lokSabhaNo(eSummonNotification.getLokSabhaNo()).sessionNo(eSummonNotification.getSessionNo())
				.summonIssuanceDate(eSummonNotification.getSummonIssuanceDate())
				.summonIssuanceDateHindiEn(eSummonNotification.getSummonIssuanceDateHindiEn())
				.summonIssuanceDateHindi(eSummonNotification.getSummonIssuanceDateHindi())
				.applicationNo(eSummonNotification.getApplicationNo()).status(eSummonNotification.getStatus())
				.templateContent(eSummonNotification.getTemplateContent())
				.templateContentHindi(eSummonNotification.getTemplateContentHindi())
				// .parentNotification(toDTO(eSummonNotification.getParentNotification())) //
				// Recursively map parent notification
				.build();
	}

	// Convert DTO to entity using the builder pattern with null checks
	public static ESummonNotification toEntity(ESummonNotificationDTO dto) {
		if (dto == null) {
			return null;
		}

		return ESummonNotification.builder().id(dto.getId()).lokSabhaNo(dto.getLokSabhaNo())
				.sessionNo(dto.getSessionNo()).summonIssuanceDate(dto.getSummonIssuanceDate())
				.summonIssuanceDateHindiEn(dto.getSummonIssuanceDateHindiEn())
				.summonIssuanceDateHindi(dto.getSummonIssuanceDateHindi())
				.applicationNo(dto.getApplicationNo()).status(dto.getStatus()).templateContent(dto.getTemplateContent())
				.templateContentHindi(dto.getTemplateContentHindi())
				// .parentNotification(toEntity(dto.getParentNotification())) // Recursively map
				// parent notification
				.build();
	}
}
