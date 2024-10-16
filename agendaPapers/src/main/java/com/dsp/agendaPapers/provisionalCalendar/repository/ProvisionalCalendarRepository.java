package com.dsp.agendaPapers.provisionalCalendar.repository;

import com.dsp.agendaPapers.provisionalCalendar.entity.ProvisionalCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvisionalCalendarRepository  extends JpaRepository<ProvisionalCalendar, Long> {

    @Query(value = "SELECT * FROM agenda_papers.provisional_calendar WHERE additional_details ->> ?1 = ?2", nativeQuery = true)
    List<ProvisionalCalendar> findByAdditionalDetailsContaining(String key, String value);

    // Find by key-value pair in balloutsDetails JSONB field
    @Query(value = "SELECT * FROM agenda_papers.provisional_calendar WHERE ballouts_details ->> ?1 = ?2", nativeQuery = true)
    List<ProvisionalCalendar> findByBalloutsDetailsContaining(String key, String value);
}

