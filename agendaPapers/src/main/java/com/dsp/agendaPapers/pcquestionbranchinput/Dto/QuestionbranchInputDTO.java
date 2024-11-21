package com.dsp.agendaPapers.pcquestionbranchinput.Dto;

import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.util.List;
import java.util.Map;
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionbranchInputDTO {

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
    private List< Map<String, Object> >otherInfo;

    private  List<Map<String, Object>> groupchartsLS;
    private  List<Map<String, Object>> groupchartsRS;
    private List< Map<String, Object>>listOfDocumentTableOffice;
    private String listOfDocumentTableOfficeFootnote;
    private String listOfDocumentTableOfficeNote;

    // I am adding to requirement by Negi is given below

    private List<Map<String, Object>> dateOfSittingInputs;
    private Date dateOfInput;
    private Boolean draftStatus;
    private Boolean generatedFlag;
    private String inputText;
    private String status;
    private String submittedByBranch;


}

