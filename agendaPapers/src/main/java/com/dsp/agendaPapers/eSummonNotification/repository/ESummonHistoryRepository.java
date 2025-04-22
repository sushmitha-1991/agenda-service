package com.dsp.agendaPapers.eSummonNotification.repository;


import com.dsp.agendaPapers.eSummonNotification.entity.ESummonHistory;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.entity.PersonalDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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


        // Note: "eSummon" is the property name in the entity.
        List<ESummonHistory> findByeSummon_Id(Long eSummonId);

        // for v2 code service impl
        // Ensure this query is correct and that the entity mappings are in place.
//        List<ESummonHistory> findByESummon(ESummonNotification eSummon);
//
//        List<ESummonHistory> findByMember(PersonalDetails member);
//
//        Optional<ESummonHistory> findByESummonAndMember(ESummonNotification eSummon, PersonalDetails member );

        // Explicit query to handle the findByESummonAndMember method
        @Query("SELECT e FROM ESummonHistory e WHERE e.eSummon = :eSummon AND e.member = :member")
        Optional<ESummonHistory> findByESummonAndMember(@Param("eSummon") ESummonNotification eSummon,
                                                        @Param("member") PersonalDetails member);


    }

