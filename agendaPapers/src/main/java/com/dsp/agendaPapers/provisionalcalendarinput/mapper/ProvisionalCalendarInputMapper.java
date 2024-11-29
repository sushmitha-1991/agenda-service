//package com.dsp.agendaPapers.provisionalcalendarinput.mapper;
//
//import com.dsp.agendaPapers.provisionalcalendarinput.Dto.ProvisionalCalendarInputDTO;
//import com.dsp.agendaPapers.provisionalcalendarinput.entity.ProvisionalCalendarInputTest;
//
//import java.util.Map;
//
//public class ProvisionalCalendarInputMapper {
//
//
//    public static ProvisionalCalendarInputTest toEntity(ProvisionalCalendarInputDTO provisionalCalendarInputDTO) {
//        return ProvisionalCalendarInputTest.builder()
//                .id(provisionalCalendarInputDTO.getId())
//                .dateOfInput(provisionalCalendarInputDTO.getDateOfInput())
//                .language(provisionalCalendarInputDTO.getLanguage())
//                .submittedBy(provisionalCalendarInputDTO.getSubmittedBy())
//                .submittedByBranch(provisionalCalendarInputDTO.getSubmittedByBranch())
//                .subject(provisionalCalendarInputDTO.getSubject())
//                .inputText(provisionalCalendarInputDTO.getInputText())
//                .footnoteText(provisionalCalendarInputDTO.getFootnoteText())
//                .approverName(provisionalCalendarInputDTO.getApproverName())
//                .approverDesignation(provisionalCalendarInputDTO.getApproverDesignation())
//                .dateOfSittingInputs(provisionalCalendarInputDTO.getDateOfSittingInputs())
//                .listOfDocument(provisionalCalendarInputDTO.getListOfDocument())
//                .draftStatus(provisionalCalendarInputDTO.getDraftStatus())
//                .content(provisionalCalendarInputDTO.getContent())
//                .generatedFlag(provisionalCalendarInputDTO.getGeneratedFlag())
//                .currentStatus(provisionalCalendarInputDTO.getStatus())
//                .applicationNo(provisionalCalendarInputDTO.getApplicationNo())
//                .currentStatus(provisionalCalendarInputDTO.getCurrentStatus())
//                .publishedDocumentUrl(provisionalCalendarInputDTO.getPublishedDocumentUrl())
//                .processInstanceId(provisionalCalendarInputDTO.getProcessInstanceId())
//                .isPublished(provisionalCalendarInputDTO.getIsPublished())
//                .lokSabhaSessionNumber(provisionalCalendarInputDTO.getLokSabhaSessionNumber())
//                .lokSabhaSession(provisionalCalendarInputDTO .getLokSabhaSession())
//                .rajyaSabhaSession(provisionalCalendarInputDTO.getRajyaSabhaSession())
//                .otherInfo((Map<String, Object>) provisionalCalendarInputDTO.getOtherInfo())
//                .applicationId(provisionalCalendarInputDTO.getApplicationId())
//                .build();
//
//    }
//
//}
////    }
////
////    public static ProvisionalCalendarInputDTO toDTO(ProvisionalCalendarInputTest provisionalCalendarInput) {
////        return ProvisionalCalendarInputDTO.builder()
////                .id(provisionalCalendarInput.getId())
////                .lokSabhaNo(provisionalCalendarInput.getLokSabhaNo())
////                .sessionNo(provisionalCalendarInput.getSessionNo())
////                .dateOfInput(provisionalCalendarInput.getDateOfInput())
////                .language(provisionalCalendarInput.getLanguage())
////                .submittedBy(provisionalCalendarInput.getSubmittedBy())
////                .submittedByBranch(provisionalCalendarInput.getSubmittedByBranch())
////                .subject(provisionalCalendarInput.getSubject())
////                .inputText(provisionalCalendarInput.getInputText())
////                .footnoteText(provisionalCalendarInput.getFootnoteText())
////                .approverName(provisionalCalendarInput.getApproverName())
////                .approverDesignation(provisionalCalendarInput.getApproverDesignation())
////                .dateOfSittingInputs(provisionalCalendarInput.getDateOfSittingInputs())
////                .status(provisionalCalendarInput.getStatus())
////                .listOfDocument(provisionalCalendarInput.getListOfDocument())
////                .draftStatus(provisionalCalendarInput.getDraftStatus())
////                .content(provisionalCalendarInput.getContent())
////                .generatedFlag(provisionalCalendarInput.getGeneratedFlag())
////                .build();
////    }
////
////}
