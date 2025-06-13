package com.dsp.agendaPapers.eSummonNotification.entity;

import com.dsp.agendaPapers.entity.Auditable;
import com.dsp.agendaPapers.util.JsonMapConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "e_summon", schema = "agenda_papers")

public class ESummonNotification extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eSummon_notifications_seq")
	@SequenceGenerator(name = "eSummon_notifications_seq", sequenceName = "eSummon_notifications_seq", allocationSize = 1)
	private Long id;

	@Column(name = "lok_sabha_no", length = 100)
	private String lokSabhaNo;

	@Column(name = "session_no", length = 100)
	private String sessionNo;

	@Column(name = "rajya_sabha_SessionNo", length = 100)

	private String rajyasabhaSessionNo;

	@Column(name = "e_summon_issuance_date")
	private LocalDate esummonIssuanceDate;

	@Column(name = "e_summon_issuance_date_hindi_en", length = 100)
	private String esummonIssuanceDateHindiEn;

	@Column(name = "e_summon_issuance_date_hindi",length = 100)
	private String esummonIssuanceDateHindi;

	@Column(name = "application_no", length = 50)
	private String applicationNo;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "e_summon_start_at")
	private LocalDateTime esummonStartAt;

	@Column(name = "status", nullable = false, length = 50)
	private String status;

	@Column(name = "template_content", columnDefinition = "TEXT")
	private String templateContent;

	@Column(name = "template_content_Hindi", columnDefinition = "TEXT")
	private String templateContentHindi;

	@Column(name = "template_type")
	private String templateType;

	@Column(name = "draft_status")
	private Boolean draftStatus = false;
	
	@Column(name = "custom_template")
	private Boolean customTemplate;
	
	@Column(name = "document_list", columnDefinition = "jsonb")
	@Convert(converter = JsonMapConverter.class)
	private List<Map<String, Object>> documentList;

	@OneToOne
	@JoinColumn(name = "parent_id")
	private ESummonNotification parentNotification;
	
	@Column(name = "house_id")
	private Long houseId;

}
