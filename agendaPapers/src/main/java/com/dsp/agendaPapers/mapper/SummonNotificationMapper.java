package com.dsp.agendaPapers.mapper;

import org.springframework.stereotype.Component;

import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;
import com.dsp.agendaPapers.summonNotification.entity.SummonNotification;

@Component
public class SummonNotificationMapper {

    // Convert entity to DTO using the builder pattern with null checks
    public static SummonNotificationDTO toDTO(SummonNotification summonNotification) {
        if (summonNotification == null) {
            return null;
        }

        return SummonNotificationDTO.builder()
                .id(summonNotification.getId())
                .lokSabhaNo(summonNotification.getLokSabhaNo())
                .sessionNo(summonNotification.getSessionNo())
                .summonIssuanceDate(summonNotification.getSummonIssuanceDate())
                .summonIssuanceDateHindiEn(summonNotification.getSummonIssuanceDateHindiEn())
                .summonIssuanceDateHindi(summonNotification.getSummonIssuanceDateHindi())
                .notificationNo(summonNotification.getNotificationNo())
                .summonStartAt(summonNotification.getSummonStartAt())
                .applicationNo(summonNotification.getApplicationNo())
                .status(summonNotification.getStatus())
                .templateContent(summonNotification.getTemplateContent())
                .TemplateType(summonNotification.getTemplateType())
              .templateContentHindi(summonNotification.getTemplateContentHindi())
               .parentNotification(toDTO(summonNotification.getParentNotification()))  // Recursively map parent notification
                .build();
    }

    // Convert DTO to entity using the builder pattern with null checks
    public static SummonNotification toEntity(SummonNotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        return SummonNotification.builder()
                .id(dto.getId())
                .lokSabhaNo(dto.getLokSabhaNo())
                .sessionNo(dto.getSessionNo())
                .summonIssuanceDate(dto.getSummonIssuanceDate())
                .summonIssuanceDateHindiEn(dto.getSummonIssuanceDateHindiEn())
                .summonIssuanceDateHindi(dto.getSummonIssuanceDateHindi())
                .notificationNo(dto.getNotificationNo())
                .summonStartAt(dto.getSummonStartAt())
                .applicationNo(dto.getApplicationNo())
                .status(dto.getStatus())
                .templateContent(dto.getTemplateContent())
                .templateType(dto.getTemplateType())
                .templateContentHindi(dto.getTemplateContentHindi())
                .parentNotification(toEntity(dto.getParentNotification()))  // Recursively map parent notification
                .build();
    }
}
