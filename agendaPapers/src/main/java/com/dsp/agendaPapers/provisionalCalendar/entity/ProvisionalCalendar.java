package com.dsp.agendaPapers.provisionalCalendar.entity;

import com.dsp.agendaPapers.config.MapToJsonConverter;
import com.dsp.agendaPapers.entity.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
@Entity
@Data
@Table(name = "provisional_data", schema = "agenda_papers")
public class ProvisionalCalendar extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provisional_data_seq")
    @SequenceGenerator(name = "provisional_data_seq", sequenceName = "agenda_papers.provisional_data_seq", allocationSize = 1)
    private Long id;


    @Column(name = "lok_sabha_no", nullable = false, length = 100)
    private String lokSabhaNo;

    @Column(name = "session_no", nullable = false, length = 100)
    private String sessionNo;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Column(name = "provisional_calendar_date", nullable = false)
    private LocalDate provisionalCalendarDate;

    @Column(name = "application_no", nullable = false, length = 50)
    private String applicationNo;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "note", nullable = false, columnDefinition = "TEXT")
    private String note;

    @Column(name = "foot_note", length = 500)
    private String footNote;

    @Column(name = "approver_name", nullable = false, length = 100)
    private String approverName;

    @Column(name = "approver_designation", nullable = false, length = 100)
    private String approverDesignation;

    @Column(name = "parent_id")
    private Integer parentId;

    @Convert(converter = MapToJsonConverter.class)
    @Column(name = "additional_details", columnDefinition = "text")
    private Map<String,Object> additionalDetails;

    @Convert(converter = MapToJsonConverter.class)
    @Column(name = "ballouts_details", columnDefinition = "text")
    private  Map<String,Object> balloutsDetails;
}
