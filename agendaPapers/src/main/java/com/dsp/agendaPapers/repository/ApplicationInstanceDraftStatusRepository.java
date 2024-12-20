package com.dsp.agendaPapers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.entity.ApplicationInstanceDraftStatus;

@Repository
public interface ApplicationInstanceDraftStatusRepository extends JpaRepository<ApplicationInstanceDraftStatus, Long> {

	ApplicationInstanceDraftStatus findByApplicationInstanceId(Long applicationInstanceId);
}
