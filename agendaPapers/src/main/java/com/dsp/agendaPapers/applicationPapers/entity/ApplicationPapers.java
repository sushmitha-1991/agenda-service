package com.dsp.agendaPapers.applicationPapers.entity;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application_Papers", schema = "agenda_papers")
public class ApplicationPapers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_papers_seq")
	@SequenceGenerator(name = "application_papers_seq", sequenceName = "application_papers_seq", allocationSize = 1)
	
	private Long id;
	@Column(name ="application_Instance_Id")
	private Long applicationInstanceId;
	@Column(name ="application_Id")
	private Long applicationId;
	@Column(name ="template_Id")
	private Long templateId;
	@Column(name = "template_content", columnDefinition = "TEXT")
    private String templateContent;

}
