package com.dsp.agendaPapers.provisionalcalendarinput.mapper;

import com.dsp.agendaPapers.provisionalcalendarinput.Dto.ProvisionalCalendarInputDTO;
import com.dsp.agendaPapers.provisionalcalendarinput.entity.ProvisionalCalendarInputTest;

public class ProvisionalCalendarInputMapper {


    public static ProvisionalCalendarInputTest toEntity(ProvisionalCalendarInputDTO provisionalCalendarInputDTO) {
        return ProvisionalCalendarInputTest.builder()
                .lokSabhaSessionNumber(provisionalCalendarInputDTO.getLokSabhaNo()) // Matches ApplicationInstance field
                .lokSabhaSession(provisionalCalendarInputDTO.getSessionNo()) // Matches ApplicationInstance field
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
                .listOfDocument(provisionalCalendarInputDTO.getListOfDocument())
                .draftStatus(provisionalCalendarInputDTO.getDraftStatus())
                .content(provisionalCalendarInputDTO.getContent())
                .generatedFlag(provisionalCalendarInputDTO.getGeneratedFlag())
                .currentStatus(provisionalCalendarInputDTO.getStatus()) // Matches ApplicationInstance field
                .build();
    }

}
//    }
//
//    public static ProvisionalCalendarInputDTO toDTO(ProvisionalCalendarInputTest provisionalCalendarInput) {
//        return ProvisionalCalendarInputDTO.builder()
//                .id(provisionalCalendarInput.getId())
//                .lokSabhaNo(provisionalCalendarInput.getLokSabhaNo())
//                .sessionNo(provisionalCalendarInput.getSessionNo())
//                .dateOfInput(provisionalCalendarInput.getDateOfInput())
//                .language(provisionalCalendarInput.getLanguage())
//                .submittedBy(provisionalCalendarInput.getSubmittedBy())
//                .submittedByBranch(provisionalCalendarInput.getSubmittedByBranch())
//                .subject(provisionalCalendarInput.getSubject())
//                .inputText(provisionalCalendarInput.getInputText())
//                .footnoteText(provisionalCalendarInput.getFootnoteText())
//                .approverName(provisionalCalendarInput.getApproverName())
//                .approverDesignation(provisionalCalendarInput.getApproverDesignation())
//                .dateOfSittingInputs(provisionalCalendarInput.getDateOfSittingInputs())
//                .status(provisionalCalendarInput.getStatus())
//                .listOfDocument(provisionalCalendarInput.getListOfDocument())
//                .draftStatus(provisionalCalendarInput.getDraftStatus())
//                .content(provisionalCalendarInput.getContent())
//                .generatedFlag(provisionalCalendarInput.getGeneratedFlag())
//                .build();
//    }
//
//}
