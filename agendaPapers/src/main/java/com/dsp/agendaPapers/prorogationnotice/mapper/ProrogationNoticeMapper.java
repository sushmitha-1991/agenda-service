package com.dsp.agendaPapers.prorogationnotice.mapper;

import org.springframework.stereotype.Component;

import com.dsp.agendaPapers.prorogationnotice.Dto.ProrogationNoticeDTO;
import com.dsp.agendaPapers.prorogationnotice.entity.ProrogationNotice;

@Component
public class ProrogationNoticeMapper {

	// Method to convert ProrogationNotice entity to ProrogationNoticeDTO
	public ProrogationNoticeDTO toDto(ProrogationNotice prorogationNotice) {
		if (prorogationNotice == null) {
			return null;
		}

		return ProrogationNoticeDTO.builder().id(prorogationNotice.getId())
				.lokSabhaNo(prorogationNotice.getLokSabhaNo())
				.lokSabhaSessionNo(prorogationNotice.getLokSabhaSessionNo())
				.rajyaSabhaSessionNo(prorogationNotice.getRajyaSabhaSessionNo())
				.issuanceDate(prorogationNotice.getIssuanceDate())
				.issuanceDateSakaEra(prorogationNotice.getIssuanceDateSakaEra())
				.issuanceDateSakaEraHindi(prorogationNotice.getIssuanceDateSakaEraHindi())
				.notificationNo(prorogationNotice.getNotificationNo())
				.applicationNo(prorogationNotice.getApplicationNo()).status(prorogationNotice.getStatus())
				.templateContent(prorogationNotice.getTemplateContent())
				.templateContentHindi(prorogationNotice.getTemplateContentHindi())
				.draftFlag(prorogationNotice.getDraftFlag())
				.templateType(prorogationNotice.getTemplateType()).build();
	}

	// Method to convert ProrogationNoticeDTO to ProrogationNotice entity
	public ProrogationNotice toEntity(ProrogationNoticeDTO prorogationNoticeDTO) {
		if (prorogationNoticeDTO == null) {
			return null;
		}

		ProrogationNotice prorogationNotice = new ProrogationNotice();
		prorogationNotice.setId(prorogationNoticeDTO.getId());
		prorogationNotice.setLokSabhaNo(prorogationNoticeDTO.getLokSabhaNo());
		prorogationNotice.setLokSabhaSessionNo(prorogationNoticeDTO.getLokSabhaSessionNo());
		prorogationNotice.setRajyaSabhaSessionNo(prorogationNoticeDTO.getRajyaSabhaSessionNo());
		prorogationNotice.setIssuanceDate(prorogationNoticeDTO.getIssuanceDate());
		prorogationNotice.setIssuanceDateSakaEra(prorogationNoticeDTO.getIssuanceDateSakaEra());
		prorogationNotice.setIssuanceDateSakaEraHindi(prorogationNoticeDTO.getIssuanceDateSakaEraHindi());
		prorogationNotice.setNotificationNo(prorogationNoticeDTO.getNotificationNo());
		prorogationNotice.setApplicationNo(prorogationNoticeDTO.getApplicationNo());
		prorogationNotice.setStatus(prorogationNoticeDTO.getStatus());
		prorogationNotice.setTemplateContent(prorogationNoticeDTO.getTemplateContent());
		prorogationNotice.setTemplateContentHindi(prorogationNoticeDTO.getTemplateContentHindi());
		prorogationNotice.setTemplateType(prorogationNoticeDTO.getTemplateType());
		prorogationNotice.setDraftFlag(prorogationNoticeDTO.getDraftFlag());
		return prorogationNotice;
	}

}
