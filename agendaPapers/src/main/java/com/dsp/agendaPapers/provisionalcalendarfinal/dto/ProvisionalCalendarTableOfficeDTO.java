package com.dsp.agendaPapers.provisionalcalendarfinal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionalCalendarTableOfficeDTO {

    private Long id;
    private List<Map<String, Object>> groupchartsLS;
    private List<Map<String, Object>> groupchartsRS;
    private List<Map<String, Object>> listOfDocumentTableOffice;
    private String listOfDocumentTableOfficeFootnote;
    private String listOfDocumentTableOfficeNote;
    private List<Map<String, Object>> dateOfSittingInputsTableOffice;
    //Changes by Sushmitha
    private LocalDateTime dateOfInputTableOffice;
    // private java.util.Date dateOfInputTableOffice;
    private String inputTextTableOffice;
    private Boolean generatedFlag;
    private Boolean draftStatus;
    private Long provisionalCalendarInputId;

}
