package com.dsp.agendaPapers.mapper;

import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import org.springframework.stereotype.Component;

@Component
public class ESummonNotificationMapper {

	// Convert entity to DTO using the builder pattern with null checks
	 public static ESummonNotificationDTO toDTO(ESummonNotification eSummonNotification) {
	        if (eSummonNotification == null) {
	            return null;
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
	                .documentList(eSummonNotification.getDocumentList())
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
	                // Uncomment if recursive mapping is needed
	                // .parentNotification(toEntity(dto.getParentNotification()))
	                .build();
	    }
}
