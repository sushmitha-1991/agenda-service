package com.dsp.agendaPapers.eSummonNotification.repository;

import com.dsp.agendaPapers.eSummonNotification.entity.PersonalDetails;
import com.dsp.agendaPapers.eSummonNotification.entity.PublishHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublishRepo extends JpaRepository<PublishHistory, Long> {
    // Optional<PublishHistory> findByApplicationNameAndForDateAndHouseAndPublishStatusTrueOrderByForDateIdDescLimit_One(String appName,LocalDate forDate,String house);

    @Query("SELECT p FROM PublishHistory p " +
       "WHERE p.applicationName = :appName " +
       "AND p.forDate = :forDate " +
       "AND p.house = :house " +
       "AND p.locale = :locale " +
       "AND p.publishStatus = true " +
       "ORDER BY p.id DESC Limit 1")
Optional<PublishHistory> findLatestPublishHistory(
    @Param("appName") String appName,
    @Param("forDate") LocalDate forDate,
    @Param("locale") String locale,
    @Param("house") String house
);
}