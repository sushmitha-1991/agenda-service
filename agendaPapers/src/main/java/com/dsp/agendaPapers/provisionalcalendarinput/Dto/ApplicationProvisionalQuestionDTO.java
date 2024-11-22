package com.dsp.agendaPapers.provisionalcalendarinput.Dto;



import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ApplicationProvisionalQuestionDTO {
    private Long id;
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
    private Date provisionalDateOfInput;
    private String language;
    private String submittedBy;
    private Date dateOfInput;
    private String submittedByBranch;
    private String subject;
    private String inputText;
    private String footnoteText;
    private String approverName;
    private String approverDesignation;
    private List<Map<String, Object>> dateOfSittingInputs;
    private List<Map<String, Object>> listOfDocument;
    private Boolean draftStatus;
    private String content;
    private Boolean generatedFlag;
    private List<Map<String, Object>> groupchartsLS;
    private List<Map<String, Object>> groupchartsRS;
    private Date questionBranchDateOfInput;
    private String questionBranchStatus;
    private List<Map<String, Object>> listOfDocumentTableOffice;
    private String listOfDocumentTableOfficeFootnote;
    private String listOfDocumentTableOfficeNote;

    // Constructor with all fields
    public ApplicationProvisionalQuestionDTO(Long id, String applicationNo, Long refId, String currentStatus,
                                             String publishedDocumentUrl, String processInstanceId, String taskId, Boolean isPublished,
                                             String lokSabhaSessionNumber, String lokSabhaSession, String rajyaSabhaSession, Date provisionalDateOfInput,
                                             String language, String submittedBy, Date dateOfInput, String submittedByBranch, String subject,
                                             String inputText, String footnoteText, String approverName, String approverDesignation,
                                             List<Map<String, Object>> dateOfSittingInputs, List<Map<String, Object>> listOfDocument, Boolean draftStatus,
                                             String content, Boolean generatedFlag, List<Map<String, Object>> groupchartsLS, List<Map<String, Object>> groupchartsRS,
                                             Date questionBranchDateOfInput, String questionBranchStatus, List<Map<String, Object>> listOfDocumentTableOffice,
                                             String listOfDocumentTableOfficeFootnote, String listOfDocumentTableOfficeNote) {
        this.id = id;
        this.applicationNo = applicationNo;
        this.refId = refId;
        this.currentStatus = currentStatus;
        this.publishedDocumentUrl = publishedDocumentUrl;
        this.processInstanceId = processInstanceId;
        this.taskId = taskId;
        this.isPublished = isPublished;
        this.lokSabhaSessionNumber = lokSabhaSessionNumber;
        this.lokSabhaSession = lokSabhaSession;
        this.rajyaSabhaSession = rajyaSabhaSession;
        this.provisionalDateOfInput = provisionalDateOfInput;
        this.language = language;
        this.submittedBy = submittedBy;
        this.dateOfInput = dateOfInput;
        this.submittedByBranch = submittedByBranch;
        this.subject = subject;
        this.inputText = inputText;
        this.footnoteText = footnoteText;
        this.approverName = approverName;
        this.approverDesignation = approverDesignation;
        this.dateOfSittingInputs = dateOfSittingInputs;
        this.listOfDocument = listOfDocument;
        this.draftStatus = draftStatus;
        this.content = content;
        this.generatedFlag = generatedFlag;
        this.groupchartsLS = groupchartsLS;
        this.groupchartsRS = groupchartsRS;
        this.questionBranchDateOfInput = questionBranchDateOfInput;
        this.questionBranchStatus = questionBranchStatus;
        this.listOfDocumentTableOffice = listOfDocumentTableOffice;
        this.listOfDocumentTableOfficeFootnote = listOfDocumentTableOfficeFootnote;
        this.listOfDocumentTableOfficeNote = listOfDocumentTableOfficeNote;
    }
}


