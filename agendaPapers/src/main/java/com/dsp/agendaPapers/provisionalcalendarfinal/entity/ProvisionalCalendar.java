package com.dsp.agendaPapers.provisionalcalendarfinal.entity;

import com.dsp.agendaPapers.entity.ApplicationInstance;
import com.dsp.agendaPapers.util.JsonMapConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.cglib.core.Local;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
// @Builder
@Data
@Entity
@Table(name = "provisional_calendar_final", schema = "agenda_papers")
public class ProvisionalCalendar extends ApplicationInstance {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provisional_calendar_final_seq")
        @SequenceGenerator(name = "provisional_calendar_final_seq", sequenceName = "provisional_calendar_final_seq", allocationSize = 1)
        private Long id;

        //changes by sushmitha
        @Column(name = "date_of_input")
        private LocalDateTime dateOfInput;

        @Column(name = "language", length = 30)
        private String language;

        @Column(name = "submitted_by", length = 100)
        private String submittedBy;

        @Column(name = "submitted_by_branch", length = 100)
        private String submittedByBranch;

        @Column(name = "subject", length = 128)
        private String subject;

        @Column(name = "input_text", length = 4096)
        private String inputText;

        @Column(name = "footnote_text", length = 512)
        private String footnoteText;

        @Column(name = "approver_name", length = 128)
        private String approverName;

        @Column(name = "approver_designation", length = 128)
        private String approverDesignation;

        @Column(name = "date_of_sitting_inputs", columnDefinition = "jsonb")
        @Convert(converter = JsonMapConverter.class)
        private List<Map<String, Object>> dateOfSittingInputs;

        @Column(name = "list_of_document", columnDefinition = "jsonb", nullable = true)
        @Convert(converter = JsonMapConverter.class)
        private List<Map<String, Object>> listOfDocument;

        @Column(name = "groupcharts_ls", columnDefinition = "jsonb")
        @Convert(converter = JsonMapConverter.class)
        private List<Map<String, Object>> groupchartsLS;

        @Column(name = "groupcharts_rs", columnDefinition = "jsonb")
        @Convert(converter = JsonMapConverter.class)
        private List<Map<String, Object>> groupchartsRS;

        @Column(name = "list_of_document_tableoffice", columnDefinition = "jsonb")
        @Convert(converter = JsonMapConverter.class)
        private List<Map<String, Object>> listOfDocumentTableOffice;

        @Column(name = "list_of_document_tableoffice_footnote")
        private String listOfDocumentTableOfficeFootnote;

        @Column(name = "list_of_document_tableoffice_note")
        private String listOfDocumentTableOfficeNote;

        @Column(name = "date_of_sitting_inputs_tableoffice", columnDefinition = "jsonb")
        @Convert(converter = JsonMapConverter.class)
        private List<Map<String, Object>> dateOfSittingInputsTableOffice;
        //Changes by Sushmitha
        @Column(name = "date_of_input_tableoffice")
        private LocalDateTime dateOfInputTableOffice;

        // private Date dateOfInputTableOffice;

        @Column(name = "input_text_tableoffice", length = 4096)
        private String inputTextTableOffice;

        @Column(name = "submitted_by_branch_tableoffice", length = 100)
        private String submittedByBranchTableOffice;

        @Column(name = "generated_flag", nullable = true)
        private Boolean generatedFlag;

        @Column(name = "content", columnDefinition = "TEXT", nullable = true)
        private String content;

        // adding the new fields for input for the input status
        @Column(name = "input_status", columnDefinition = "TEXT", nullable = true)
        private String inputStatus;

        // for the remarks
        @Column(name = "remarks", columnDefinition = "TEXT", nullable = true)
        private String remarks;

        // coloumm for the draft status

        // @Column(name= "drfat_status", columnDefinition = "boolean default false",
        // nullable = true )
        // private Boolean draftStatus;

        @Column(name = "status",length = 20, nullable = true)
        private String status;

        //Changes by Sushmitha
        @Column(name = "lok_sabha_session_no", nullable = true)
        private Long lokSabhaSessionNo;
        @Column(name = "rajya_sabha_session_no", nullable = true)
        private Long rsSessionNo;
        //changes on 07-01-2025
        @Column(name = "input_ref_no", length = 50)
        private String inputRefNo;


}
