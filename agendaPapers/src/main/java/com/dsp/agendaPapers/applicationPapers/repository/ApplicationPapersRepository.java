package com.dsp.agendaPapers.applicationPapers.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.applicationPapers.entity.ApplicationPapers;

@Repository
public interface ApplicationPapersRepository extends JpaRepository<ApplicationPapers, Long> {
	
    Optional<ApplicationPapers> findByApplicationInstanceId(Long applicationInstanceId);

	Optional<ApplicationPapers> findByApplicationIdAndApplicationInstanceId(Long applicationId, Long id);


}
