package com.dsp.agendaPapers.provisionalcalendarinput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.provisionalcalendarinput.entity.ProvisionalCalendarInput;

@Repository
public interface ProvisionalCalendarInputRepository extends JpaRepository<ProvisionalCalendarInput,Long> {
}
