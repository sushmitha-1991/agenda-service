package com.dsp.agendaPapers.pcquestionbranchinput.mapper;


import com.dsp.agendaPapers.pcquestionbranchinput.Dto.QuestionbranchInputDTO;
import com.dsp.agendaPapers.pcquestionbranchinput.entity.QuestionbranchInput;

import java.util.List;
import java.util.Map;

public class QuestionbranchInputMapper {

    public static QuestionbranchInput toEntity(QuestionbranchInputDTO dto) {
        return QuestionbranchInput.builder()
                .id(dto.getId())
                .applicationNo(dto.getApplicationNo())
                .refId(dto.getRefId())
                .currentStatus(dto.getCurrentStatus())
                .publishedDocumentUrl(dto.getPublishedDocumentUrl())
                .processInstanceId(dto.getProcessInstanceId())
                .taskId(dto.getTaskId())
                .isPublished(dto.getIsPublished())
                .lokSabhaSessionNumber(dto.getLokSabhaSessionNumber())
                .lokSabhaSession(dto.getLokSabhaSession())
                .rajyaSabhaSession(dto.getRajyaSabhaSession())
                .otherInfo((Map<String, Object>) dto.getOtherInfo())
                .groupchartsLS(dto.getGroupchartsLS())
                .groupchartsRS(dto.getGroupchartsRS())
                .listOfDocumentTableOffice(dto.getListOfDocumentTableOffice())
                .listOfDocumentTableOfficeFootnote(dto.getListOfDocumentTableOfficeFootnote())
                .listOfDocumentTableOfficeNote(dto.getListOfDocumentTableOfficeNote())
                .dateOfSittingInputs(dto.getDateOfSittingInputs())
                .dateOfInput(dto.getDateOfInput())
                .draftStatus(dto.getDraftStatus())
                .generatedFlag(dto.getGeneratedFlag())
                .inputText(dto.getInputText())
                .status(dto.getStatus())
                .submittedByBranch(dto.getSubmittedByBranch())
                .build();
    }

    public static QuestionbranchInputDTO toDTO(QuestionbranchInput entity) {
        return QuestionbranchInputDTO.builder()
                .id(entity.getId())
                .applicationNo(entity.getApplicationNo())
                .refId(entity.getRefId())
                .currentStatus(entity.getCurrentStatus())
                .publishedDocumentUrl(entity.getPublishedDocumentUrl())
                .processInstanceId(entity.getProcessInstanceId())
                .taskId(entity.getTaskId())
                .isPublished(entity.getIsPublished())
                .lokSabhaSessionNumber(entity.getLokSabhaSessionNumber())
                .lokSabhaSession(entity.getLokSabhaSession())
                .rajyaSabhaSession(entity.getRajyaSabhaSession())
                .otherInfo((List<Map<String, Object>>) entity.getOtherInfo())
                .groupchartsLS((List<Map<String, Object>>) entity.getGroupchartsLS())
                .groupchartsRS((List<Map<String, Object>>) entity.getGroupchartsRS())
                .listOfDocumentTableOffice((List<Map<String, Object>>) entity.getListOfDocumentTableOffice())
                .listOfDocumentTableOfficeFootnote(entity.getListOfDocumentTableOfficeFootnote())
                .listOfDocumentTableOfficeNote(entity.getListOfDocumentTableOfficeNote())
                .dateOfSittingInputs(entity.getDateOfSittingInputs())
                .dateOfInput(entity.getDateOfInput())
                .draftStatus(entity.getDraftStatus())
                .generatedFlag(entity.getGeneratedFlag())
                .inputText(entity.getInputText())
                .status(entity.getStatus())
                .submittedByBranch(entity.getSubmittedByBranch())
                .build();
    }
}

