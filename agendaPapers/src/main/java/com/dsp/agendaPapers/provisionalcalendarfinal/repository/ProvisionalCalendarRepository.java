package com.dsp.agendaPapers.provisionalcalendarfinal.repository;

import com.dsp.agendaPapers.provisionalcalendarfinal.entity.ProvisionalCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProvisionalCalendarRepository extends JpaRepository<ProvisionalCalendar, Long> {

    @Query("SELECT p FROM ProvisionalCalendar p WHERE p.dateOfInput BETWEEN :startDate AND :endDate")
    List<ProvisionalCalendar> findByDateOfInputBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // for inputstatus by application id
    Optional<ProvisionalCalendar> findByApplicationId(Long applicationId);


}
