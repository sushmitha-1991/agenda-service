package com.dsp.agendaPapers.provisionalcalendarinput.repository;

import com.dsp.agendaPapers.provisionalcalendarinput.entity.ProvisionalCalendarInputTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



import java.sql.Date;
import java.util.List;

//@EnableJpaRepositories
// extra I am adding the @EnableJPaRepo
@Repository
public interface ProvisionalCalendarInputRepository extends JpaRepository<ProvisionalCalendarInputTest,Long> {


//    @Query("SELECT p FROM ProvisionalCalendarInputTest p WHERE p.submittedByBranch = :submittedByBranch " +
//            "AND p.status = :status " +
//            "AND p.dateOfInput BETWEEN :startDate AND :endDate")
//    List<ProvisionalCalendarInputTest> findByBranchAndStatusAndDateRange(
//            String submittedByBranch, String status, Date startDate, Date endDate);


//    // Method to fetch records by a single status or multiple statuses
//    List<ProvisionalCalendarInputTest> findByStatusIn(List<String> statuses);
}
