package com.dsp.agendaPapers.provisionalcalendarfinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.provisionalcalendarfinal.entity.InputInstance;

@Repository
public interface InputInstanceRepository extends JpaRepository<InputInstance, Long> {

}
