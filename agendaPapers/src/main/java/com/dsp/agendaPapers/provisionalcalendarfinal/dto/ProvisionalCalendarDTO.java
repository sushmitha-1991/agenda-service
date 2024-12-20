package com.dsp.agendaPapers.provisionalcalendarfinal.dto;

import com.dsp.agendaPapers.dto.ApplicationInstanceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class ProvisionalCalendarDTO {

    private Long id;
    // private Date dateOfInput;
// changes by Sushmitha
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
    private List<Map<String, Object>> groupchartsLS;
    private List<Map<String, Object>> groupchartsRS;
    private List<Map<String, Object>> listOfDocumentTableOffice;
    private String listOfDocumentTableOfficeFootnote;
    private String listOfDocumentTableOfficeNote;
    private List<Map<String, Object>> dateOfSittingInputsTableOffice;
    //Changes by Sushmitha
    private LocalDateTime dateOfInputTableOffice;
    // private Date dateOfInputTableOffice;
    private String inputTextTableOffice;
    private String rajyaSabhaSessionNo;
    private Boolean generatedFlag;
    private String content;

    // adding new field for return
    private String inputStatus;
    private String remarks;
    private String refNumber;

    // adding the new fiels draft status
    // private Boolean draftStatus;

    // Application Instace

    private String applicationNo;
    private String currentStatus;
    private String publishedDocumentUrl;
    // private String processInstanceId;
    private String taskId;
    private Boolean isPublished;
    private String lokSabhaSessionNumber;
    private String lokSabhaSession;
    private String rajyaSabhaSession;
    private Long applicationId;

    private ApplicationInstanceDTO applicationInstanceDTO;
    //changes by Sushmitha
    private InputInstanceDTO inputInstanceDTO;

    // process insatnce will be remove from everywhere

}
