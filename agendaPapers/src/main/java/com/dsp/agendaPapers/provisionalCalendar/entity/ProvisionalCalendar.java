package com.dsp.agendaPapers.provisionalCalendar.entity;

import com.dsp.agendaPapers.entity.ApplicationInstance;
import com.dsp.agendaPapers.util.ListToJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Data
@Table(name = "provisional_calendar", schema = "agenda_papers")
public class ProvisionalCalendar extends ApplicationInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provisional_data_seq")
    @SequenceGenerator(name = "provisional_calendar_seq", sequenceName = "agenda_papers.provisional_calendar_seq", allocationSize = 1)
    private Long id;

    @Column(name = "lok_sabha_no", nullable = false, length = 100)
    private String lokSabhaNo;

    @Column(name = "session_no", nullable = false, length = 100)
    private String sessionNo;

    @Column(name = "rajya_sabha_session_no", length = 100)
    private String rajyaSabhaSessionNo;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "generated_doc_ids", columnDefinition = "jsonb")
    @Convert(converter = ListToJsonConverter.class)
    private List<Integer> generatedDocId;

    @Column(name = "draft_status")
    private Boolean draftStatus;

}
