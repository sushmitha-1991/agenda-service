package com.dsp.agendaPapers.provisionalCalendar.service;

import com.dsp.agendaPapers.provisionalCalendar.Dto.ProvisionalCalendarDTO;

import java.util.List;

public interface ProvisionalCalendarService {


        // Create a new Provisional Calendar
        ProvisionalCalendarDTO createProvisionalCalendar(ProvisionalCalendarDTO provisionalCalendarDTO);

        // Get Provisional Calendar by ID
        ProvisionalCalendarDTO getProvisionalCalendarById(Long id);

        // Get all Provisional Calendars
        List<ProvisionalCalendarDTO> getAllProvisionalCalendars();


        // Update an existing Provisional Calendar
        ProvisionalCalendarDTO updateProvisionalCalendar(Long id, ProvisionalCalendarDTO provisionalCalendarDTO);

        // Delete Provisional Calendar by ID
        void deleteProvisionalCalendar(Long id);

        // Get Provisional Calendar by Lok Sabha No.
        List<ProvisionalCalendarDTO> getProvisionalCalendarsByLokSabhaNo(String lokSabhaNo);

        // Get Provisional Calendar by Session No.
        List<ProvisionalCalendarDTO> getProvisionalCalendarsBySessionNo(String sessionNo);

        // Get Provisional Calendar by Rajya Sabha Session No.
        List<ProvisionalCalendarDTO> getProvisionalCalendarsByRajyaSabhaSessionNo(String rajyaSabhaSessionNo);

        // Get Provisional Calendars by draft status
        List<ProvisionalCalendarDTO> getProvisionalCalendarsByDraftStatus(Boolean draftStatus);
    }




