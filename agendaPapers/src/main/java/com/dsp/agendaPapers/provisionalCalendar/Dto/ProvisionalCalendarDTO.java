package com.dsp.agendaPapers.provisionalCalendar.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionalCalendarDTO {

    private Long id;
    private String lokSabhaNo;
    private String sessionNo;
    private Long branchId;
    private LocalDate provisionalCalendarDate;
    private String applicationNo;
    private String status;
    private String note;
    private String footNote;
    private String approverName;
    private String approverDesignation;
    private Integer parentId;
    private Map<String, Object> additionalDetails;
    private Map<String, Object> balloutsDetails;
}
