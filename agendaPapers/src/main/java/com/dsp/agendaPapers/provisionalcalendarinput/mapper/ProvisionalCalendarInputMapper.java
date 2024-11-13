package com.dsp.agendaPapers.provisionalcalendarinput.mapper;

import com.dsp.agendaPapers.provisionalcalendarinput.Dto.ProvisionalCalendarInputDTO;
import com.dsp.agendaPapers.provisionalcalendarinput.entity.ProvisionalCalendarInput;

public class ProvisionalCalendarInputMapper {


    public static ProvisionalCalendarInput toEntity(ProvisionalCalendarInputDTO provisionalCalendarInputDTO) {
        return ProvisionalCalendarInput.builder()
                .lokSabhaNo(provisionalCalendarInputDTO.getLokSabhaNo())
                .sessionNo(provisionalCalendarInputDTO.getSessionNo())
                .dateOfInput(provisionalCalendarInputDTO.getDateOfInput())
                .language(provisionalCalendarInputDTO.getLanguage())
                .submittedBy(provisionalCalendarInputDTO.getSubmittedBy())
                .submittedByBranch(provisionalCalendarInputDTO.getSubmittedByBranch())
                .subject(provisionalCalendarInputDTO.getSubject())
                .inputText(provisionalCalendarInputDTO.getInputText())
                .footnoteText(provisionalCalendarInputDTO.getFootnoteText())
                .approverName(provisionalCalendarInputDTO.getApproverName())
                .approverDesignation(provisionalCalendarInputDTO.getApproverDesignation())
                .dateOfSittingInputs(provisionalCalendarInputDTO.getDateOfSittingInputs())
                .status(provisionalCalendarInputDTO.getStatus())
                .listOfDocument(provisionalCalendarInputDTO.getListOfDocument())
                .draftStatus(provisionalCalendarInputDTO.getDraftStatus())
                .content(provisionalCalendarInputDTO.getContent())
                .build();
    }

    public static ProvisionalCalendarInputDTO toDTO(ProvisionalCalendarInput provisionalCalendarInput) {
        return ProvisionalCalendarInputDTO.builder()
                .id(provisionalCalendarInput.getId())
                .lokSabhaNo(provisionalCalendarInput.getLokSabhaNo())
                .sessionNo(provisionalCalendarInput.getSessionNo())
                .dateOfInput(provisionalCalendarInput.getDateOfInput())
                .language(provisionalCalendarInput.getLanguage())
                .submittedBy(provisionalCalendarInput.getSubmittedBy())
                .submittedByBranch(provisionalCalendarInput.getSubmittedByBranch())
                .subject(provisionalCalendarInput.getSubject())
                .inputText(provisionalCalendarInput.getInputText())
                .footnoteText(provisionalCalendarInput.getFootnoteText())
                .approverName(provisionalCalendarInput.getApproverName())
                .approverDesignation(provisionalCalendarInput.getApproverDesignation())
                .dateOfSittingInputs(provisionalCalendarInput.getDateOfSittingInputs())
                .status(provisionalCalendarInput.getStatus())
                .listOfDocument(provisionalCalendarInput.getListOfDocument())
                .draftStatus(provisionalCalendarInput.getDraftStatus())
                .content(provisionalCalendarInput.getContent())
                .build();
    }

}
