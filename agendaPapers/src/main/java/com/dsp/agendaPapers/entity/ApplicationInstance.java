package com.dsp.agendaPapers.entity;

import com.dsp.agendaPapers.util.MapJsonConverter;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ApplicationInstance", schema = "master")
public class ApplicationInstance extends Auditable  {

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

	@Convert(converter = MapJsonConverter.class)
	@Column(name = "other_info", columnDefinition = "jsonb")
	private Map<String, Object> otherInfo;
}