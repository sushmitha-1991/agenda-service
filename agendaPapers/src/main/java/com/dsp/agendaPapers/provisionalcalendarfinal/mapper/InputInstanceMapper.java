package com.dsp.agendaPapers.provisionalcalendarfinal.mapper;

import com.dsp.agendaPapers.provisionalcalendarfinal.dto.InputInstanceDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.entity.InputInstance;

public class InputInstanceMapper {
    public static InputInstance toEntity(InputInstanceDTO dto) {
        return InputInstance.builder()
                .id(dto.getId())
                .applicationId(dto.getApplicationId())
                .applicationNo(dto.getApplicationNo())
                .currentStatus(dto.getCurrentStatus())
                .refId(dto.getRefId())
                .pendingAt(dto.getPendingAt())
                .documentDate(dto.getDocumentDate())
                .lokSabhaSession(dto.getLokSabhaSession())
                .lokSabhaSessionNumber(dto.getLokSabhaSessionNumber())
                .rajyaSabhaSession(dto.getRajyaSabhaSession())
                .build();
    }

    public static InputInstanceDTO toDTO(InputInstance entity) {
        return InputInstanceDTO.builder()
                .id(entity.getId())
                .applicationId(entity.getApplicationId())
                .applicationNo(entity.getApplicationNo())
                .refId(entity.getRefId())
                .currentStatus(entity.getCurrentStatus())
                .documentDate(entity.getDocumentDate())
                .pendingAt(entity.getPendingAt())
                .lokSabhaSession(entity.getLokSabhaSession())
                .lokSabhaSessionNumber(entity.getLokSabhaSessionNumber())
                .rajyaSabhaSession(entity.getRajyaSabhaSession())
                .build();

    }
}
