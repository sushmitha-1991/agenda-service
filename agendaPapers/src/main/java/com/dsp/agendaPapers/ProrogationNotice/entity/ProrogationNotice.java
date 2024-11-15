//package com.dsp.agendaPapers.ProrogationNotice.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import java.time.LocalDateTime;
//import java.util.Date;
//
//
//
//@Data
//@Entity
//@Table(name = "prorogation_notice", schema = "agenda_papers")
//public class ProrogationNotice {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "lok_sabha_no", nullable = false)
//    private Long lokSabhaNo;
//
//    @Column(name = "lob_sabha_session_no", nullable = false)
//    private Long lobSabhaSessionNo;
//
//    @Column(name = "rajya_sabha_session_no", nullable = false)
//    private Long rajyaSabhaSessionNo;
//
//    @Column(name = "issuance_date", nullable = false)
//    private Date issuanceDate;
//
//    @Column(name = "issuance_date_saka_era", nullable = false, length = 50)
//    private String issuanceDateSakaEra;
//
//    @Column(name = "issuance_date_saka_era_hindi", nullable = false, length = 50)
//    private String issuanceDateSakaEraHindi;
//
//    @Column(name = "notification_no", nullable = false, length = 100)
//    private String notificationNo;
//
//    @Column(name = "ref_no")
//    private String refNo;
//
//    @Column(name = "template_content")
//    private String templateContent;
//
//    @Column(name = "template_content_hindi")
//    private String templateContentHindi;
//
//    @Column(name = "draft_flag", nullable = false, columnDefinition = "boolean default false")
//    private Boolean draftFlag;
//
//    @Column(name = "status", nullable = false, length = 50)
//    private String status;
//
//    @Column(name = "is_active", columnDefinition = "boolean default true")
//    private Boolean isActive = true;
//
//    @Column(name = "is_deleted", columnDefinition = "boolean default false")
//    private Boolean isDeleted = false;
//
//    @Column(name = "created_at", columnDefinition = "timestamp default current_timestamp")
//    private LocalDateTime createdAt;
//
//    @Column(name = "created_by")
//    private Long createdBy;
//
//    @Column(name = "updated_at", columnDefinition = "timestamp default current_timestamp")
//    private LocalDateTime updatedAt;
//
//    @Column(name = "updated_by")
//    private Long updatedBy;
//
//    @ManyToOne
//    @JoinColumn(name = "lok_sabha_no", insertable = false, updatable = false)
//    private LokSabhaMaster lokSabhaMaster;
//
//    @ManyToOne
//    @JoinColumn(name = "lob_sabha_session_no", insertable = false, updatable = false)
//    private LokSabhaSessionMaster lokSabhaSessionMaster;
//
//    @ManyToOne
//    @JoinColumn(name = "rajya_sabha_session_no", insertable = false, updatable = false)
//    private RajyaSabhaSessionMaster rajyaSabhaSessionMaster;
//}
//

