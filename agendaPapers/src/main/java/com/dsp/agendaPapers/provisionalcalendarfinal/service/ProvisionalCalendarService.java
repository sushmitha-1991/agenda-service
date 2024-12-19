package com.dsp.agendaPapers.provisionalcalendarfinal.service;

import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarDTO;
//import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarGenerateDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarGenerateDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarInputDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarTableOfficeDTO;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;


public interface ProvisionalCalendarService {

    ProvisionalCalendarDTO createProvisionalCalendar(@Valid ProvisionalCalendarDTO provisionalCalendar);

    ProvisionalCalendarDTO getProvisionalCalendarById(Long id);

    List<ProvisionalCalendarDTO> getAllProvisionalCalendars();

    ProvisionalCalendarDTO updateProvisionalCalendar(Long id, @Valid ProvisionalCalendarDTO updatedProvisionalCalendar);

    void deleteProvisionalCalendar(Long id);

    void softDeleteProvisionalCalendar(Long id);

    // Method to create a new Provisional Calendar Input

    // Create a new Provisional Calendar Input
    ProvisionalCalendarInputDTO createProvisionalCalendarInput(ProvisionalCalendarInputDTO inputDTO);

    ProvisionalCalendarTableOfficeDTO createProvisionalCalendarTableOffice(ProvisionalCalendarTableOfficeDTO provisionalCalendarTableOfficeDTO);

    ProvisionalCalendarGenerateDTO createProvisionalCalendarGenerate(ProvisionalCalendarGenerateDTO provisionalCalendarGenerateDTO);


    List<ProvisionalCalendarDTO> getAllProvisionalCalendarsBetweenDates(LocalDate startDate, LocalDate endDate, String inputStatus, String submittedBy);
    // for the input status
    ProvisionalCalendarInputDTO updateInputStatus(Long id, String inputStatus);

//    List<ProvisionalCalendarDTO> findByInputStatusAndSubmittedByAndDateOfInputBetween(
//            LocalDate startDate, LocalDate endDate, String inputStatus, String submittedBy);
}
