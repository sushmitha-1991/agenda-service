package com.dsp.agendaPapers.provisionalcalendarinput.service;

import com.dsp.agendaPapers.provisionalcalendarinput.Dto.ProvisionalCalendarInputDTO;


import java.sql.Date;
import java.util.List;

public interface ProvisionalCalendarInputService {

    ProvisionalCalendarInputDTO createProvisionalCalendarInput(ProvisionalCalendarInputDTO provisionalCalendarInputDTO);

    ProvisionalCalendarInputDTO getProvisionalCalendarInputById(Long id);

    List<ProvisionalCalendarInputDTO> getAllProvisionalCalendarInputs();

    ProvisionalCalendarInputDTO updateProvisionalCalendarInput(Long id, ProvisionalCalendarInputDTO provisionalCalendarInputDTO);

    void deleteProvisionalCalendarInput(Long id);

    ProvisionalCalendarInputDTO saveProvisionalCalendarInput(ProvisionalCalendarInputDTO dto, Boolean draftStatus);

    List<ProvisionalCalendarInputDTO> getProvisionalData(String submittedByBranch, String status, Date startDate, Date endDate);

    // Method to fetch Provisional Calendar Inputs by statuses
    List<ProvisionalCalendarInputDTO> getProvisionalInputsByStatus(List<String> statuses);

}
