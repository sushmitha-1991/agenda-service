package com.dsp.agendaPapers.provisionalcalendarfinal.mapper;

import com.dsp.agendaPapers.dto.ApplicationInstanceDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.entity.ProvisionalCalendar;

public class ProvisionalCalendarDTOMapper {

  public static ProvisionalCalendar toEntity(ProvisionalCalendarDTO dto) {
    return ProvisionalCalendar.builder()
        .id(dto.getId())
        .rajyaSabhaSession(dto.getRajyaSabhaSessionNo())
        .dateOfInput(dto.getDateOfInput())
        .language(dto.getLanguage())
        .submittedBy(dto.getSubmittedBy())
        .submittedByBranch(dto.getSubmittedByBranch())
        .subject(dto.getSubject())
        .inputText(dto.getInputText())
        .footnoteText(dto.getFootnoteText())
        .approverName(dto.getApproverName())
        .approverDesignation(dto.getApproverDesignation())
        .dateOfSittingInputs(dto.getDateOfSittingInputs())
        .listOfDocument(dto.getListOfDocument())
        .groupchartsLS(dto.getGroupchartsLS())
        .groupchartsRS(dto.getGroupchartsRS())
        .listOfDocumentTableOffice(dto.getListOfDocumentTableOffice())
        .listOfDocumentTableOfficeFootnote(dto.getListOfDocumentTableOfficeFootnote())
        .listOfDocumentTableOfficeNote(dto.getListOfDocumentTableOfficeNote())
        .dateOfInput(dto.getDateOfInputTableOffice())
        .inputText(dto.getInputTextTableOffice())
        .generatedFlag(dto.getGeneratedFlag())
        .content(dto.getContent())
        // adding the new field input status
        .inputStatus(dto.getInputStatus())
        .remarks(dto.getRemarks())
        // draft status
        // .draftStatus(dto.getDraftStatus())

        // Application instance
        .id(dto.getId())
        .applicationNo(dto.getApplicationNo())
        .currentStatus(dto.getCurrentStatus())
        .publishedDocumentUrl(dto.getPublishedDocumentUrl())
        // .processInstanceId(dto.getProcessInstanceId())
        .isPublished(dto.getIsPublished())
        .lokSabhaSessionNumber(dto.getLokSabhaSessionNumber())
        .lokSabhaSession(dto.getLokSabhaSession())
        .rajyaSabhaSession(dto.getRajyaSabhaSession())
        .applicationId(dto.getApplicationId())
        .documentDate(dto.getDateOfInput().toLocalDate())
    
        .build();
  }

  public static ProvisionalCalendarDTO toDTO(ProvisionalCalendar entity) {
    return ProvisionalCalendarDTO.builder()
        .id(entity.getId())
        .rajyaSabhaSessionNo(entity.getRajyaSabhaSession())
        .dateOfInput(entity.getDateOfInput())
        .language(entity.getLanguage())
        .submittedBy(entity.getSubmittedBy())
        .submittedByBranch(entity.getSubmittedByBranch())
        .subject(entity.getSubject())
        .inputText(entity.getInputText())
        .footnoteText(entity.getFootnoteText())
        .approverName(entity.getApproverName())
        .approverDesignation(entity.getApproverDesignation())
        .dateOfSittingInputs(entity.getDateOfSittingInputs())
        .listOfDocument(entity.getListOfDocument())
        .groupchartsLS(entity.getGroupchartsLS())
        .groupchartsRS(entity.getGroupchartsRS())
        .listOfDocumentTableOffice(entity.getListOfDocumentTableOffice())
        .listOfDocumentTableOfficeFootnote(entity.getListOfDocumentTableOfficeFootnote())
        .listOfDocumentTableOfficeNote(entity.getListOfDocumentTableOfficeNote())
        .dateOfInputTableOffice(entity.getDateOfInput())
        .inputTextTableOffice(entity.getInputText())
        .generatedFlag(entity.getGeneratedFlag())
        .content(entity.getContent())

        // adding the new field input status
        .inputStatus(entity.getInputStatus())
        .remarks(entity.getRemarks())
        // draft status
        // .draftStatus(entity.getDraftStatus())
        // application Instance
        .id(entity.getId())
        .applicationNo(entity.getApplicationNo())
        .currentStatus(entity.getCurrentStatus())
        .publishedDocumentUrl(entity.getPublishedDocumentUrl())
        // .processInstanceId(entity.getProcessInstanceId())
        .isPublished(entity.getIsPublished())
        .lokSabhaSessionNumber(entity.getLokSabhaSessionNumber())
        .lokSabhaSession(entity.getLokSabhaSession())
        .rajyaSabhaSession(entity.getRajyaSabhaSession())
        .applicationId(entity.getApplicationId())
        
        .applicationInstanceDTO(ApplicationInstanceDTO.builder()
            .id(entity.getId())
            .applicationNo(entity.getApplicationNo())
            .applicationId(entity.getApplicationId())
            .currentStatus(entity.getCurrentStatus())
            .refId(entity.getRefId())
            .lokSabhaSessionNumber(entity.getLokSabhaSessionNumber())
            .lokSabhaSession(entity.getLokSabhaSession())
            .rajyaSabhaSession(entity.getRajyaSabhaSession())
            .documentDate(entity.getDocumentDate())
            .build())
        .build();
  }
}
