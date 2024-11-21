package com.dsp.agendaPapers.pcquestionbranchinput.entity;

import com.dsp.agendaPapers.entity.ApplicationInstance;
import com.dsp.agendaPapers.util.JsonMapConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "question_branch_table", schema = "agenda_papers")
public class QuestionbranchInput extends ApplicationInstance {

    @Column(name = "groupcharts_ls", columnDefinition = "jsonb")
    @Convert(converter = JsonMapConverter.class)
    private List<Map<String, Object> > groupchartsLS;

    @Column(name = "groupcharts_rs", columnDefinition = "jsonb")
    @Convert(converter = JsonMapConverter.class)
    private List<Map<String, Object>> groupchartsRS;

    @Column(name = "list_of_document_tableoffice", columnDefinition = "jsonb")
    @Convert(converter = JsonMapConverter.class)
    private List< Map<String, Object> >listOfDocumentTableOffice;

    @Column(name = "list_of_document_tableoffice_footnote")
    private String listOfDocumentTableOfficeFootnote;

    @Column(name = "list_of_document_tableoffice_note")
    private String listOfDocumentTableOfficeNote;

   // According to requirment by negi

    @Column(name = "date_of_sitting_inputs_tableoffice", columnDefinition = "jsonb")
    @Convert(converter = JsonMapConverter.class)
    private List<Map<String, Object>> dateOfSittingInputs;

    @Column(name = "date_of_input_tableoffice", nullable = false)
    private Date dateOfInput;

    @Column(name = "draft_status_tableoffice", nullable = true)
    private Boolean draftStatus;

    @Column(name = "generated_flag_tableoffice", nullable = false)
    private Boolean generatedFlag;

    @Column(name = "input_text_tableoffice", nullable = false, length = 4096)
    private String inputText;

    @Column(name = "status_tableoffice", length = 50)
    private String status;

    @Column(name = "submitted_by_branch_tableoffice", nullable = false, length = 100)
    private String submittedByBranch;



}

