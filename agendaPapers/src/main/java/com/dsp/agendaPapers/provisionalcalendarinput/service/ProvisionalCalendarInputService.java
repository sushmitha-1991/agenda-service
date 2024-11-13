package com.dsp.agendaPapers.provisionalcalendarinput.service;

import com.dsp.agendaPapers.provisionalcalendarinput.Dto.ProvisionalCalendarInputDTO;

import java.util.List;

public interface ProvisionalCalendarInputService {

    ProvisionalCalendarInputDTO createProvisionalCalendarInput(ProvisionalCalendarInputDTO provisionalCalendarInputDTO);

    ProvisionalCalendarInputDTO getProvisionalCalendarInputById(Long id);

    List<ProvisionalCalendarInputDTO> getAllProvisionalCalendarInputs();

    ProvisionalCalendarInputDTO updateProvisionalCalendarInput(Long id, ProvisionalCalendarInputDTO provisionalCalendarInputDTO);

    void deleteProvisionalCalendarInput(Long id);

    ProvisionalCalendarInputDTO saveProvisionalCalendarInput(ProvisionalCalendarInputDTO dto, Boolean draftStatus);

}
