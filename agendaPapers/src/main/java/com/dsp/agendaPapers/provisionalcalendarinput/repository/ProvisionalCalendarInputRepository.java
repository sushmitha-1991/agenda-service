package com.dsp.agendaPapers.provisionalcalendarinput.repository;

import com.dsp.agendaPapers.provisionalcalendarinput.enitity.ProvisionalCalendarInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvisionalCalendarInputRepository extends JpaRepository<ProvisionalCalendarInput,Long> {
}
