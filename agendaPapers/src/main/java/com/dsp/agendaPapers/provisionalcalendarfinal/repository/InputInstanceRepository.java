package com.dsp.agendaPapers.provisionalcalendarfinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.provisionalcalendarfinal.entity.InputInstance;

import java.util.Optional;

@Repository
public interface InputInstanceRepository extends JpaRepository<InputInstance, Long> {
    @Query("SELECT i FROM InputInstance i WHERE i.refId = :refId")
    Optional<InputInstance> findByRefId(@Param("refId") Long refId);

	InputInstance findByApplicationNo(String applicationNo);

}
