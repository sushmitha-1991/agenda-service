package com.dsp.agendaPapers.prorogationnotice.entity;

import com.dsp.agendaPapers.entity.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Audited
@Entity
@Data
@Table(name = "prorogation_notice", schema = "agenda_papers")
public class ProrogationNotice extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prorogation_notice_seq")
	@SequenceGenerator(name = "prorogation_notice_seq", sequenceName = "agenda_papers.prorogation_notice_seq", allocationSize = 1)

	private Long id;

	@Column(name = "lok_sabha_no")
	private String lokSabhaNo;

	@Column(name = "lok_sabha_session_no")
	private String lokSabhaSessionNo;

	@Column(name = "rajya_sabha_session_no")
	private String rajyaSabhaSessionNo;

	@Column(name = "issuance_date", nullable = false)
	private LocalDate issuanceDate;

	@Column(name = "issuance_date_saka_era", nullable = false, length = 50)
	private String issuanceDateSakaEra;

	@Column(name = "issuance_date_saka_era_hindi", nullable = false, length = 50)
	private String issuanceDateSakaEraHindi;

	@Column(name = "notification_no", nullable = false, length = 100)
	private String notificationNo;

	@Column(name = "application_no", length = 50)
	private String applicationNo;

	@Column(name = "template_content")
	private String templateContent;

	@Column(name = "template_content_hindi")
	private String templateContentHindi;

	@Column(name = "draft_flag", nullable = false)
	private Boolean draftFlag = false;

	@Column(name = "status", nullable = false, length = 50)
	private String status;

	@Column(name = "template_type")
	private String templateType;

}
