package com.dsp.agendaPapers.eSummonHistory.repository;


import com.dsp.agendaPapers.eSummonHistory.entity.ESummonHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ESummonHistoryRepository extends JpaRepository<ESummonHistory, Long> {

    // Correct return type as Optional<ESummonHistory>
    Optional<ESummonHistory> findById(Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO agenda_papers.e_summon_history (e_summon_id, member_id, document_uuid, mail_sent_at, mail_status, retry_count) " +
            "VALUES (:eSummonId, :memberId, :documentUuid, :mailSentAt, :mailStatus, :retryCount)", nativeQuery = true)
    void insertESummonHistory(@Param("eSummonId") Long eSummonId,
                              @Param("memberId") Long memberId,
                              @Param("documentUuid") UUID documentUuid,
                              @Param("mailSentAt") LocalDateTime mailSentAt,
                              @Param("mailStatus") String mailStatus,
                              @Param("retryCount") int retryCount);
}

