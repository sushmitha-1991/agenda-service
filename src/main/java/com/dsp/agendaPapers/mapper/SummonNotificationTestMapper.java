//package com.dsp.agendaPapers.mapper;
//
//import org.springframework.stereotype.Component;
//
//import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;
//import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;
//import com.dsp.agendaPapers.summonNotification.entity.SummonNotificationTest;
//
//@Component
//public class SummonNotificationTestMapper {
//
//	public SummonNotificationTest toEntity(SummonNotificationDTO dto) {
//		if (dto == null) {
//			return null;
//		}
//
//		// Create a new instance of SummonNotificationTest
//		SummonNotificationTest summonNotificationTest = SummonNotificationTest.builder().lokSabhaNo(dto.getLokSabhaNo())
//				.sessionNo(dto.getSessionNo()).summonIssuanceDate(dto.getSummonIssuanceDate())
//				.summonIssuanceDateHindiEn(dto.getSummonIssuanceDateHindiEn())
//				.summonIssuanceDateHindi(dto.getSummonIssuanceDateHindi()).notificationNo(dto.getNotificationNo())
//				.summonStartAt(dto.getSummonStartAt()).status(dto.getStatus()).templateContent(dto.getTemplateContent())
//				.templateContentHindi(dto.getTemplateContentHindi())
//				.parentNotification(dto.getParentNotification() != null ? toEntity(dto.getParentNotification()) : null)
//				.build();
//
//		// Map the applicationInstanceDTO fields
//		ApplicationInstanceDTO applicationInstanceDTO = dto.getApplicationInstanceDTO();
//		if (applicationInstanceDTO != null) {
//			summonNotificationTest.setApplicationNo(applicationInstanceDTO.getApplicationNo());
//			summonNotificationTest.setCurrentStatus(applicationInstanceDTO.getCurrentStatus());
//			summonNotificationTest.setProcessInstanceId(applicationInstanceDTO.getProcessInstanceId());
//			summonNotificationTest.setIsPublished(applicationInstanceDTO.getIsPublished());
//			summonNotificationTest.setLokSabhaSessionNumber(applicationInstanceDTO.getLokSabhaSessionNumber());
//			summonNotificationTest.setLokSabhaSession(applicationInstanceDTO.getLokSabhaSession());
//			summonNotificationTest.setRajyaSabhaSession(applicationInstanceDTO.getRajyaSabhaSession());
//			summonNotificationTest.setRefId(applicationInstanceDTO.getRefId());
//			summonNotificationTest.setTaskId(applicationInstanceDTO.getTaskId());
//			summonNotificationTest.setPublishedDocumentUrl(applicationInstanceDTO.getPublishedDocumentUrl());
//		}
//
//		return summonNotificationTest; // Add return statement
//	}
//
//	// Map from Entity to DTO using Builder pattern
//	public SummonNotificationDTO toDto(SummonNotificationTest entity) {
//		if (entity == null) {
//			return null;
//		}
//
//		// DTO builder mapping including ApplicationInstance fields
//		return SummonNotificationDTO.builder().id(entity.getId()).lokSabhaNo(entity.getLokSabhaNo())
//				.sessionNo(entity.getSessionNo()).summonIssuanceDate(entity.getSummonIssuanceDate())
//				.summonIssuanceDateHindiEn(entity.getSummonIssuanceDateHindiEn())
//				.summonIssuanceDateHindi(entity.getSummonIssuanceDateHindi()).notificationNo(entity.getNotificationNo())
//				.summonStartAt(entity.getSummonStartAt()).status(entity.getStatus())
//				.templateContent(entity.getTemplateContent()).templateContentHindi(entity.getTemplateContentHindi())
//				.applicationInstanceDTO(ApplicationInstanceDTO.builder()
//						.applicationId(entity.getApplication() != null ? entity.getApplication().getId() : null)
//						.applicationNo(entity.getApplicationNo()).refId(entity.getRefId())
//						.currentStatus(entity.getCurrentStatus()).publishedDocumentUrl(entity.getPublishedDocumentUrl())
//						.processInstanceId(entity.getProcessInstanceId()).taskId(entity.getTaskId())
//						.isPublished(entity.getIsPublished()).lokSabhaSessionNumber(entity.getLokSabhaSessionNumber())
//						.lokSabhaSession(entity.getLokSabhaSession()).rajyaSabhaSession(entity.getRajyaSabhaSession())
//						.build())
//				.parentNotification(
//						entity.getParentNotification() != null ? toDto(entity.getParentNotification()) : null) // recursive
//																												// mapping
//																												// for
//																												// parent
//																												// notifications
//				.build();
//	}
//}
