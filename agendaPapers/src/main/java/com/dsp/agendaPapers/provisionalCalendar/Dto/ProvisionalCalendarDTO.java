package com.dsp.agendaPapers.provisionalCalendar.Dto;

import com.dsp.agendaPapers.dto.ApplicationInstanceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
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

    // Application Instance
    private String applicationNo;
    private String currentStatus;
    private String publishedDocumentUrl;
    private String processInstanceId;
    private String taskId;
    private Boolean isPublished;
    private String lokSabhaSessionNumber;
    private String lokSabhaSession;
    private String rajyaSabhaSession;
    private Long applicationId;
    private List< Map<String, Object> >otherInfo;
//    private Long applicationId;
    private ApplicationInstanceDTO applicationInstanceDTO;




}
