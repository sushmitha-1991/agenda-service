package com.dsp.agendaPapers.eSummonNotification.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.dsp.agendaPapers.entity.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "publish_history", schema = "workflow")
public class PublishHistory extends Auditable {

    @Id
    private Long id;

    private String lokSabhaNo;
    private String lokSabhaSessionNo;
    private String rajyaSabhaSessionNo;
    private Long applicationId;
    private String applicationName;
    private String applicationNo;
    private UUID publishUuid;
    private String fileType;
    private String publishUrl;
    private LocalDate forDate;
    private Boolean publishStatus;
    private String locale;
    private String house;

}
