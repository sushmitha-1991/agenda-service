package com.dsp.agendaPapers.provisionalcalendarinput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.provisionalcalendarinput.entity.ProvisionalCalendarInput;

import java.sql.Date;
import java.util.List;

@Repository
public interface ProvisionalCalendarInputRepository extends JpaRepository<ProvisionalCalendarInput,Long> {

    @Query("SELECT p FROM ProvisionalCalendarInput p WHERE p.submittedByBranch = :submittedByBranch " +
            "AND p.status = :status " +
            "AND p.dateOfInput BETWEEN :startDate AND :endDate")
    List<ProvisionalCalendarInput> findByBranchAndStatusAndDateRange(
            String submittedByBranch, String status, Date startDate, Date endDate);
}
