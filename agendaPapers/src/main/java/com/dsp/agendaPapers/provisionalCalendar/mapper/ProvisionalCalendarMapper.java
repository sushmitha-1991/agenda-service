package com.dsp.agendaPapers.provisionalCalendar.mapper;


import com.dsp.agendaPapers.provisionalCalendar.Dto.ProvisionalCalendarDTO;
import com.dsp.agendaPapers.provisionalCalendar.entity.ProvisionalCalendar;

public class ProvisionalCalendarMapper {
    public static ProvisionalCalendar toEntity(ProvisionalCalendarDTO provisionalCalendarDTO) {
        return ProvisionalCalendar.builder()
                .lokSabhaNo(provisionalCalendarDTO.getLokSabhaNo())
                .sessionNo(provisionalCalendarDTO.getSessionNo())
                .branchId(provisionalCalendarDTO.getBranchId())
                .provisionalCalendarDate(provisionalCalendarDTO.getProvisionalCalendarDate())
                .applicationNo(provisionalCalendarDTO.getApplicationNo())
                .status(provisionalCalendarDTO.getStatus())
                .note(provisionalCalendarDTO.getNote())
                .footNote(provisionalCalendarDTO.getFootNote())
                .approverName(provisionalCalendarDTO.getApproverName())
                .approverDesignation(provisionalCalendarDTO.getApproverDesignation())
                .parentId(provisionalCalendarDTO.getParentId())
                .additionalDetails(provisionalCalendarDTO.getAdditionalDetails())
                .balloutsDetails(provisionalCalendarDTO.getBalloutsDetails())
                .build();
    }

    public static ProvisionalCalendarDTO toDTO(ProvisionalCalendar provisionalCalendar) {
        return ProvisionalCalendarDTO.builder()
                .id(provisionalCalendar.getId())
                .lokSabhaNo(provisionalCalendar.getLokSabhaNo())
                .sessionNo(provisionalCalendar.getSessionNo())
                .branchId(provisionalCalendar.getBranchId())
                .provisionalCalendarDate(provisionalCalendar.getProvisionalCalendarDate())
                .applicationNo(provisionalCalendar.getApplicationNo())
                .status(provisionalCalendar.getStatus())
                .note(provisionalCalendar.getNote())
                .footNote(provisionalCalendar.getFootNote())
                .approverName(provisionalCalendar.getApproverName())
                .approverDesignation(provisionalCalendar.getApproverDesignation())
                .parentId(provisionalCalendar.getParentId())
                .additionalDetails(provisionalCalendar.getAdditionalDetails())
                .balloutsDetails(provisionalCalendar.getBalloutsDetails())
                .build();
    }
}
