package com.dsp.agendaPapers.provisionalCalendar.service;

import com.dsp.agendaPapers.provisionalCalendar.Dto.ProvisionalCalendarDTO;

import java.util.List;

public interface ProvisionalCalendarService {

    ProvisionalCalendarDTO createProvisionalCalendar(ProvisionalCalendarDTO provisionalCalendarDTO);

    ProvisionalCalendarDTO getProvisionalCalendarById(Long id);

    List<ProvisionalCalendarDTO> getAllProvisionalCalendars();

    ProvisionalCalendarDTO updateProvisionalCalendar(Long id, ProvisionalCalendarDTO provisionalCalendarDTO);

    void deleteProvisionalCalendar(Long id);

}
