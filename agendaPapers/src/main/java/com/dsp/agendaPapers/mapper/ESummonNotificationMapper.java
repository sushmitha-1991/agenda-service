package com.dsp.agendaPapers.mapper;

import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.util.Utils;

import org.springframework.stereotype.Component;

@Component
public class ESummonNotificationMapper {

	// Convert entity to DTO using the builder pattern with null checks
	 public static ESummonNotificationDTO toDTO(ESummonNotification entity) {
	        if (entity == null) {
	            return null;
	        }

	        return ESummonNotificationDTO.builder()
	                .id(entity.getId())
	                .lokSabhaNo(entity.getLokSabhaNo())
	                .sessionNo(entity.getSessionNo())
	                .rajyasabhaSessionNo(entity.getRajyasabhaSessionNo())
	                .esummonIssuanceDate(entity.getEsummonIssuanceDate())
	                .esummonIssuanceDateHindi(entity.getEsummonIssuanceDateHindi())
	                .esummonIssuanceDateHindiEn(entity.getEsummonIssuanceDateHindiEn())
	                .applicationNo(entity.getApplicationNo())
	                .status(entity.getStatus())
	                .templateContent(entity.getTemplateContent())
	                .templateContentHindi(entity.getTemplateContentHindi())
	                .templateType(entity.getTemplateType())
	                .draftStatus(entity.getDraftStatus())
	                .customTemplate(entity.getCustomTemplate())
	                .esummonStartAt(entity.getEsummonStartAt())
	                .houseId(entity.getHouseId())
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
	                .houseId(dto.getHouseId() == null ? Utils.getHouseId() : dto.getHouseId())
	                .build();
	    }
}
