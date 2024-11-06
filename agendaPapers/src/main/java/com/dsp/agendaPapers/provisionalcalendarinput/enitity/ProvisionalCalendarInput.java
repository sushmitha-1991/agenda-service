package com.dsp.agendaPapers.provisionalcalendarinput.enitity;

import com.dsp.agendaPapers.config.MapToJsonConverter;
import com.dsp.agendaPapers.entity.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.sql.Date;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
@Entity
@Data
@Table(name = "provisional_calendar_input", schema = "agenda_papers")
public class ProvisionalCalendarInput extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provisional_calendar_seq")
    @SequenceGenerator(name = "provisional_calendar_seq", sequenceName = "agenda_papers.provisional_calendar_seq", allocationSize = 1)
    private Long id;


    @Column(name = "lok_sabha_no", nullable = false, length = 100)
    private String lokSabhaNo;

    @Column(name = "session_no", nullable = false, length = 100)
    private String sessionNo;

    @Column(name = "date_of_input", nullable = false)
    private Date dateOfInput;

    @Column(name = "language", nullable = false, length = 30)
    private String language;

    @Column(name = "submitted_by", nullable = false, length = 100)
    private String submittedBy;

    @Column(name = "submitted_by_branch", nullable = false, length = 100)
    private String submittedByBranch;

    @Column(name = "subject", nullable = false, length = 128)
    private String subject;

    @Column(name = "input_text", nullable = false, length = 4096)
    private String inputText;

    @Column(name = "footnote_text", nullable = false, length = 512)
    private String footnoteText;

    @Column(name = "approver_name", nullable = false, length = 128)
    private String approverName;

    @Column(name = "approver_designation", nullable = false, length = 128)
    private String approverDesignation;

    @Convert(converter = MapToJsonConverter.class)
    @Column(name = "dateOfSittingInputs", columnDefinition = "text")
    private Map<String, Object> dateOfSittingInputs;




}

