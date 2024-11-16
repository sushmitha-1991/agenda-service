package com.dsp.agendaPapers.provisionalCalendar.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionalCalendarDTO {

    private Long id;

    private String lokSabhaNo;

    private String sessionNo;

    private String rajyaSabhaSessionNo;

    private String content;

    private List<Integer> generatedDocId;

    private Boolean draftStatus;
}
