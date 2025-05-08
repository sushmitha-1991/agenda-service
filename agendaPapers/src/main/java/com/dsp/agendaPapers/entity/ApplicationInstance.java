package com.dsp.agendaPapers.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import com.dsp.agendaPapers.util.MapJsonConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "application_instance", schema = "master")
public class ApplicationInstance extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_instance_seq")
	@SequenceGenerator(name = "application_instance_seq", sequenceName = "master.application_instance_seq", allocationSize = 1)
	private Long id;

	@Column(name = "application_id", nullable = false)
	private Long applicationId;

	@Column(name = "application_no", nullable = false, length = 255)
	private String applicationNo;

	@Column(name = "ref_id")
	private Long refId;

	@Column(name = "current_status", nullable = false, length = 100)
	private String currentStatus;

	@Column(name = "published_document_url", length = 255)
	private String publishedDocumentUrl;

	@Column(name = "process_instance_Id", length = 255)
	private String processInstanceId;

	@Column(name = "is_published")
	private Boolean isPublished;

	@Column(name = "lok_sabha_session", length = 30)
	private String lokSabhaSession;

	@Column(name = "lok_sabha_session_number", length = 30)
	private String lokSabhaSessionNumber;

	@Column(name = "rajya_sabha_session", length = 30)
	private String rajyaSabhaSession;
	
	@Convert(converter = MapJsonConverter.class)
	@Column(name = "other_info", columnDefinition = "jsonb")
	private Map<String, Object> otherInfo;

	@Column(name = "document_date")
	private LocalDate documentDate;

	@Column(name = "html_content_en", columnDefinition = "text")
	private String htmlContentEnglish;
	
	@Column(name = "html_content_hi", columnDefinition = "text")
	private String htmlContentHindi;
}
