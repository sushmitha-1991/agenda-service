package com.dsp.agendaPapers.entity;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "ApplicationInstance", schema = "agenda_papers")
@Inheritance(strategy = InheritanceType.JOINED)
public class ApplicationInstance extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_instance_seq")
	@SequenceGenerator(name = "application_instance_seq", sequenceName = "application_instance_seq", allocationSize = 1)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "application_id", nullable = true)
	private Application application;

	@Column(name = "applicationNo", nullable = true, length = 255)
	private String applicationNo;

	@Column(name = "ref_Id")
	private Long refId;

	@Column(name = "currentStatus", nullable = true, length = 100)
	private String currentStatus;

	@Column(name = "published_documentUrl", length = 255)
	private String publishedDocumentUrl;

	@Column(name = "process_Instance_Id", length = 255)
	private String processInstanceId;

	@Column(name = "task_id", length = 255, nullable = true)
	private String taskId;

	@Column(name = "is_published", nullable = true)
	private Boolean isPublished;

	@Column(name = "lok_sabha_session_number", length = 30, nullable = true)
	private String lokSabhaSessionNumber;

	@Column(name = "lok_sabha_session", length = 30, nullable = true)
	private String lokSabhaSession;

	@Column(name = "rajya_sabha_session", length = 30, nullable = true)
	private String rajyaSabhaSession;

	@Convert(converter = MapJsonConverter.class)
	@Column(name = "other_info", columnDefinition = "jsonb")
	private Map<String, Object> otherInfo;
}
