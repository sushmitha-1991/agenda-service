//package com.dsp.agendaPapers.prorogationnotice.mapper;
//
//import com.dsp.agendaPapers.prorogationnotice.Dto.ProrogationNoticeDTO;
//import com.dsp.agendaPapers.prorogationnotice.entity.ProrogationNotice;
//
//public class ProrogationNoticeMapper {
//
//    public static ProrogationNotice toEntity (ProrogationNoticeDTO prorogationNoticeDTO){
//
//        return ProrogationNotice.builder()
//                .id(prorogationNoticeDTO.getId())
//                .lokSabhaNo(prorogationNoticeDTO .getLokSabhaNo())
//                .lobSabhaSessionNo(prorogationNoticeDTO.getLobSabhaSessionNo())
//                .rajyaSabhaSessionNo(prorogationNoticeDTO.getRajyaSabhaSessionNo())
//                .issuanceDate(prorogationNoticeDTO.getIssuanceDate())
//                .issuanceDateSakaEra(prorogationNoticeDTO.getIssuanceDateSakaEra())
//                .issuanceDateSakaEraHindi(prorogationNoticeDTO.getIssuanceDateSakaEraHindi())
//                .notificationNo(prorogationNoticeDTO.getNotificationNo())
//                .refNo(prorogationNoticeDTO.getRefNo())
//                .templateContent(prorogationNoticeDTO.getTemplateContent())
//                .templateContentHindi(prorogationNoticeDTO.getTemplateContentHindi())
//                .draftFlag(prorogationNoticeDTO.getDraftFlag())
//                .status(prorogationNoticeDTO.getStatus())
//                .build();
//    }
//
//    public static ProrogationNoticeDTO toDTO(ProrogationNotice prorogationNotice){
//
//        return ProrogationNoticeDTO.builder()
//
//                .build();
//    }
//}
