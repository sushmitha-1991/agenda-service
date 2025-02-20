package com.dsp.agendaPapers.eSummonHistory.mapper;




//@Component
//public class ESummonHistoryMapper {
//
//    // Convert Entity to DTO
//    public ESummonHistoryDTO toDTO(ESummonHistory entity) {
//        if (entity == null) {
//            return null;
//        }
//        return ESummonHistoryDTO.builder()
//                .id(entity.getId())
//                .eSummonId(entity.getESummon() != null ? entity.getESummon().getId() : null)
//                .memberId(entity.getMember() != null ? entity.getMember().getId() : null)
//                .documentUuid(entity.getDocumentUuid())
//                .mailSentAt(entity.getMailSentAt())
//                .mailStatus(entity.getMailStatus())
//                .lastRetryDatetime(entity.getLastRetryDatetime())
//                .retryCount(entity.getRetryCount())
//                .remarks(entity.getRemarks())
//                .build();
//    }
//
//    // Convert DTO to Entity
//    public ESummonHistory toEntity(ESummonHistoryDTO dto, ESummonNotification eSummon, PersonalDetails member) {
//        if (dto == null) {
//            return null;
//        }
//        return ESummonHistory.builder()
//                .id(dto.getId())
//                .eSummon(eSummon)
//                .member(member)
//                .documentUuid(dto.getDocumentUuid())
//                .mailSentAt(dto.getMailSentAt())
//                .mailStatus(dto.getMailStatus())
//                .lastRetryDatetime(dto.getLastRetryDatetime())
//                .retryCount(dto.getRetryCount())
//                .remarks(dto.getRemarks())
//                .build();
//    }

// new Code


import com.dsp.agendaPapers.eSummonHistory.dto.ESummonHistoryDTO;

import com.dsp.agendaPapers.eSummonHistory.entity.ESummonHistory;
import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonHistory.entity.PersonalDetails;
import org.springframework.stereotype.Component;

@Component
public class ESummonHistoryMapper {

    // Convert Entity to DTO
    public ESummonHistoryDTO toDTO(ESummonHistory entity) {
        if (entity == null) {
            return null;
        }

        // Map the ESummonNotification entity to ESummonNotificationDTO
        ESummonNotificationDTO eSummonNotificationDTO = null;
        if (entity.getESummon() != null) {
            eSummonNotificationDTO = ESummonNotificationDTO.builder()
                    .id(entity.getESummon().getId())
                    .lokSabhaNo(entity.getESummon().getLokSabhaNo())
                    .sessionNo(entity.getESummon().getSessionNo())
                    .rajyasabhaSessionNo(entity.getESummon().getRajyasabhaSessionNo())
                    .esummonIssuanceDate(entity.getESummon().getEsummonIssuanceDate())
                    .esummonIssuanceDateHindi(entity.getESummon().getEsummonIssuanceDateHindi())
                    .esummonIssuanceDateHindiEn(entity.getESummon().getEsummonIssuanceDateHindiEn())
                    .applicationNo(entity.getESummon().getApplicationNo())
                    .esummonStartAt(entity.getESummon().getEsummonStartAt())
                    .status(entity.getESummon().getStatus())
                    .templateContent(entity.getESummon().getTemplateContent())
                    .templateContentHindi(entity.getESummon().getTemplateContentHindi())
                    .templateType(entity.getESummon().getTemplateType())
                    .draftStatus(entity.getESummon().getDraftStatus())
                    .customTemplate(entity.getESummon().getCustomTemplate())
                    .build();
        }

        return ESummonHistoryDTO.builder()
                .id(entity.getId())
                .eSummon(eSummonNotificationDTO)  // Set the nested DTO
                .memberId(entity.getMember() != null ? entity.getMember().getId() : null)
                .documentUuid(entity.getDocumentUuid())
                .mailSentAt(entity.getMailSentAt())
                .mailStatus(entity.getMailStatus())
                .lastRetryDatetime(entity.getLastRetryDatetime())
                .retryCount(entity.getRetryCount())
                .remarks(entity.getRemarks())
                .build();
    }

    // Convert DTO to Entity
    public ESummonHistory toEntity(ESummonHistoryDTO dto, ESummonNotification eSummon, PersonalDetails member) {
        if (dto == null) {
            return null;
        }
        return ESummonHistory.builder()
                .id(dto.getId())
                .eSummon(eSummon)
                .member(member)
                .documentUuid(dto.getDocumentUuid())
                .mailSentAt(dto.getMailSentAt())
                .mailStatus(dto.getMailStatus())
                .lastRetryDatetime(dto.getLastRetryDatetime())
                .retryCount(dto.getRetryCount())
                .remarks(dto.getRemarks())
                .build();
    }
}



