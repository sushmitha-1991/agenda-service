package com.dsp.agendaPapers.provisionalcalendarfinal.repository;

import com.dsp.agendaPapers.provisionalcalendarfinal.entity.ProvisionalCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvisionalCalendarRepository extends JpaRepository<ProvisionalCalendar, Long> {
}
