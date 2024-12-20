package com.dsp.agendaPapers.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "application_instance_draft_status", schema = "master")
public class ApplicationInstanceDraftStatus extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_instance_draft_status_seq")
	@SequenceGenerator(name = "application_instance_draft_status_seq", sequenceName = "master.application_instance_draft_status_id_seq", allocationSize = 1)
	private Long id;

	@Column(name = "application_instance_id")
	private Long applicationInstanceId;

	@Column(name = "form_id", length = 100)
	private String formId;

	@Column(name = "task_id", length = 100)
	private String taskId;

	@Column(name = "other", columnDefinition = "jsonb")
	private String other;

}