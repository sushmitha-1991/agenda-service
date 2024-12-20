package com.dsp.agendaPapers.provisionalcalendarfinal.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.provisionalcalendarfinal.entity.ProvisionalCalendar;

@Repository
public interface ProvisionalCalendarRepository extends JpaRepository<ProvisionalCalendar, Long> {

        // @Query("SELECT p FROM ProvisionalCalendar p WHERE p.dateOfInput BETWEEN
        // :startDate AND :endDate")
        // List<ProvisionalCalendar> findByDateOfInputBetween(@Param("startDate")
        // LocalDateTime startDate,
        // @Param("endDate") LocalDateTime endDate);
        //
        // // for inputstatus by application id
        //// Optional<ProvisionalCalendar> findByApplicationId(Long applicationId);
        //// @Query("SELECT p FROM ProvisionalCalendar p WHERE p.inputStatus= :
        // inputStatus")
        //// List<ProvisionalCalendar> findByInputStatus(@Param("inputStatus") String
        // inputStatus);
        //
        // @Query("SELECT p FROM ProvisionalCalendar p WHERE (:inputStatus IS NULL OR
        // p.inputStatus = :inputStatus)")
        // List<ProvisionalCalendar> findByInputStatus(@Param("inputStatus") String
        // inputStatus);
        //
        //
        //
        //
        // @Query("SELECT p FROM ProvisionalCalendar p WHERE (:inputStatus IS NULL OR
        // p.inputStatus = :inputStatus) AND p.dateOfInput BETWEEN :startDate AND
        // :endDate")
        // List<ProvisionalCalendar> findByInputStatusAndDateOfInputBetween(
        // @Param("inputStatus") String inputStatus,
        // @Param("startDate") LocalDateTime startDate,
        // @Param("endDate") LocalDateTime endDate);

        // working code above all code

        // List<ProvisionalCalendar> findByInputStatusAndDateOfInputBetween(String
        // inputStatus, LocalDateTime startDate, LocalDateTime endDate);

        // new Implemention

        List<ProvisionalCalendar> findByInputStatus(String inputStatus);

        List<ProvisionalCalendar> findByDateOfInputBetween(LocalDateTime startDate, LocalDateTime endDate);

        @Query("SELECT p FROM ProvisionalCalendar p WHERE (:inputStatus IS NULL OR p.inputStatus = :inputStatus) AND p.dateOfInput BETWEEN :startDate AND :endDate")
        List<ProvisionalCalendar> findByInputStatusAndDateOfInputBetween(
                        @Param("inputStatus") String inputStatus,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // for submitted by in get All for testing

        // All data for the testing

        List<ProvisionalCalendar> findByInputStatusAndSubmittedBy(String inputStatus, String submittedBy);

        List<ProvisionalCalendar> findBySubmittedBy(String submittedBy);

        List<ProvisionalCalendar> findBySubmittedByAndDateOfInputBetween(String submittedBy, LocalDateTime startDate,
                        LocalDateTime endDate);

        List<ProvisionalCalendar> findByInputStatusAndSubmittedByAndDateOfInputBetween(
                        String inputStatus, String submittedBy, LocalDateTime startDate, LocalDateTime endDate);

}
