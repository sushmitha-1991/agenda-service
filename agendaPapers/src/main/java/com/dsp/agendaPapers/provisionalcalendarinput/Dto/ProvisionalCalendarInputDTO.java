package com.dsp.agendaPapers.provisionalcalendarinput.Dto;

import com.dsp.agendaPapers.dto.ApplicationInstanceDTO;
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

    // latest code
    private Long id;
//    private String lokSabhaNo;
//    private String sessionNo;
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
    private Boolean generatedFlag;

    // Fields inherited from ApplicationInstance
    private String applicationNo;
    private Long refId;
    private String currentStatus;
    private String publishedDocumentUrl;
    private String processInstanceId;
    private String taskId;
    private Boolean isPublished;
    private String lokSabhaSessionNumber;
    private String lokSabhaSession;
    private String rajyaSabhaSession;
    private Map<String, Object> otherInfo;
    private Long applicationId;

    private ApplicationInstanceDTO applicationInstanceDTO;


}
