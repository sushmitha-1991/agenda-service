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
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ApplicationInstance", schema = "master")
public class ApplicationInstance extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_Instance_seq")
	@SequenceGenerator(name = "application_instance_seq", sequenceName = "application_instance_seq", allocationSize = 1)
	private Long id;

	@Column(name = "application_id", nullable = false)
	private Long applicationId;

	@Column(name = "applicationNo", nullable = false, length = 255)
	private String applicationNo;

	@Column(name = "ref_Id")
	private Long refId;

	@Column(name = "currentStatus", nullable = false, length = 100)
	private String currentStatus;

	@Column(name = "published_documentUrl", length = 255)
	private String publishedDocumentUrl;

	@Column(name = "process_Instance_Id", length = 255)
	private String processInstanceId;

	@Column(name = "is_published")
	private Boolean isPublished;

	@Column(name = "lok_sabha_session", length = 30)
	private String lokSabhaSession;

	@Column(name = "lok_sabha_session_number", length = 30)
	private String lokSabhaSessionNumber;

	@Column(name = "rajya_sabha_session", length = 30)
	private String rajyaSabhaSession;

	@Column(name = "task_id", length = 255)
	private String taskId;

	@Convert(converter = MapJsonConverter.class)
	@Column(name = "other_info", columnDefinition = "jsonb")
	private Map<String, Object> otherInfo;

	@Column(name = "draft")
	private Boolean draft;

	// adding the coloumn
	@Column(name = "document_date")
	private LocalDate documentDate;
	//Changes done by Sushmitha
	// private Date documentDate;


}