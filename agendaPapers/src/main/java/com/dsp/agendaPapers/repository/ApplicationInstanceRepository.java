package com.dsp.agendaPapers.repository;

import com.dsp.agendaPapers.entity.ApplicationInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationInstanceRepository extends JpaRepository<ApplicationInstance, Long> {
}
