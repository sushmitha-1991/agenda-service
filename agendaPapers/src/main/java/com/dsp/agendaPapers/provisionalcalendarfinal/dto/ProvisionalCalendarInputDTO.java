package com.dsp.agendaPapers.provisionalcalendarfinal.dto;

import com.dsp.agendaPapers.dto.ApplicationInstanceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionalCalendarInputDTO {

    private Long id;
    //changes by sushmitha
    private LocalDateTime dateOfInput;
    private String language;
    private String submittedBy;
    private String submittedByBranch;
    private String subject;
    private String inputText;
    private String footnoteText;
    private String approverName;
    private String approverDesignation;
    private List<Map<String, Object>> dateOfSittingInputs;
    private List<Map<String, Object>> listOfDocument;
    private Long applicationId;
    private String lokSabhaSession;
    private String lokSabhaSessionNumber;
    private String rajyaSabhaSession;
    private String status;
    // Additional session details
    private Long lokSabhaSessionNo;
    private Long rsSessionNo;
    // adding the only input status field
    private String inputStatus;
    private String remarks;
    

    //

    private ApplicationInstanceDTO applicationInstanceDTO;

    //Input Instance DTO

    private InputInstanceDTO inputInstanceDTO;

    // application instance

}
