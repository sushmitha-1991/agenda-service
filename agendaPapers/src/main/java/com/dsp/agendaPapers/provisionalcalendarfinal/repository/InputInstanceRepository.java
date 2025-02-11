package com.dsp.agendaPapers.provisionalcalendarfinal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.provisionalcalendarfinal.entity.InputInstance;

import jakarta.transaction.Transactional;

@Repository
public interface InputInstanceRepository extends JpaRepository<InputInstance, Long> {

    @Query("SELECT i FROM InputInstance i WHERE i.refId = :refId")
    Optional<InputInstance> findByRefId(@Param("refId") Long refId);

    InputInstance findByApplicationNo(String applicationNo);

    @Query(value = "SELECT COUNT(*) FROM dms.document_metadata WHERE uuid = :uuid", nativeQuery = true)
    int checkUuidExists(@Param("uuid") String uuid);
    

    @Modifying
    @Transactional
    @Query(value = "UPDATE dms.document_metadata SET application_no = :applicationNo WHERE uuid = :uuid", 
    nativeQuery = true)
    void updateApplicationNoByUUID(@Param("uuid") String uuid, @Param("applicationNo") String applicationNo);
}
