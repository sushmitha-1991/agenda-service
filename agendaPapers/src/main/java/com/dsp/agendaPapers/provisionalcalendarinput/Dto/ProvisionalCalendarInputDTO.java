package com.dsp.agendaPapers.provisionalcalendarinput.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionalCalendarInputDTO {

    private Long id;
    private String lokSabhaNo;
    private String sessionNo;

    @NotNull(message = "Date of input is required")
    private Date dateOfInput;
    private String language;
    private String submittedBy;
    private String submittedByBranch;
    private String subject;
    private String inputText;
    private String footnoteText;
    private String approverName;
    private String approverDesignation;

    private List<Map<String, Object>> dateOfSittingInputs;

    private String status;

    private List<Map<String, Object>> listOfDocument;

    private Boolean draftStatus;

    private String content;
}
