package com.dsp.agendaPapers.provisionalcalendarinput.service;

import com.dsp.agendaPapers.provisionalcalendarinput.Dto.ProvisionalCalendarInputDTO;
import com.dsp.agendaPapers.provisionalcalendarinput.enitity.ProvisionalCalendarInput;
import com.dsp.agendaPapers.provisionalcalendarinput.repository.ProvisionalCalendarInputRepository;
import com.dsp.agendaPapers.response.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProvisionalCalendarInputServiceImpl implements ProvisionalCalendarInputService{

    @Autowired
    private ProvisionalCalendarInputRepository provisionalCalendarInputRepository;

    @Override
    public ProvisionalCalendarInputDTO createProvisionalCalendarInput(ProvisionalCalendarInputDTO provisionalCalendarInputDTO) {
        ProvisionalCalendarInput provisionalCalendarInput = ProvisionalCalendarInput.builder()
                .lokSabhaNo(provisionalCalendarInputDTO.getLokSabhaNo())
                .sessionNo(provisionalCalendarInputDTO.getSessionNo())
                .dateOfInput(provisionalCalendarInputDTO.getDateOfInput())
                .language(provisionalCalendarInputDTO.getLanguage())
                .submittedBy(provisionalCalendarInputDTO.getSubmittedBy())
                .submittedByBranch(provisionalCalendarInputDTO.getSubmittedByBranch())
                .subject(provisionalCalendarInputDTO.getSubject())
                .inputText(provisionalCalendarInputDTO.getInputText())
                .footnoteText(provisionalCalendarInputDTO.getFootnoteText())
                .approverName(provisionalCalendarInputDTO.getApproverName())
                .approverDesignation(provisionalCalendarInputDTO.getApproverDesignation())
                .build();

        ProvisionalCalendarInput savedProvisionalCalendarInput = provisionalCalendarInputRepository.save(provisionalCalendarInput);
        return mapToDto(savedProvisionalCalendarInput);
    }

    @Override
    public ProvisionalCalendarInputDTO getProvisionalCalendarInputById(Long id) {
        ProvisionalCalendarInput provisionalCalendarInput = provisionalCalendarInputRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProvisionalCalendarInput not found with the given ID"));
        return mapToDto(provisionalCalendarInput);
    }

    @Override
    public List<ProvisionalCalendarInputDTO> getAllProvisionalCalendarInputs() {
        return provisionalCalendarInputRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProvisionalCalendarInputDTO updateProvisionalCalendarInput(Long id, ProvisionalCalendarInputDTO provisionalCalendarInputDTO) {
        return provisionalCalendarInputRepository.findById(id)
                .map(existingProvisionalCalendarInput -> {
                    existingProvisionalCalendarInput.setLokSabhaNo(provisionalCalendarInputDTO.getLokSabhaNo());
                    existingProvisionalCalendarInput.setSessionNo(provisionalCalendarInputDTO.getSessionNo());
                    existingProvisionalCalendarInput.setDateOfInput(provisionalCalendarInputDTO.getDateOfInput());
                    existingProvisionalCalendarInput.setLanguage(provisionalCalendarInputDTO.getLanguage());
                    existingProvisionalCalendarInput.setSubmittedBy(provisionalCalendarInputDTO.getSubmittedBy());
                    existingProvisionalCalendarInput.setSubmittedByBranch(provisionalCalendarInputDTO.getSubmittedByBranch());
                    existingProvisionalCalendarInput.setSubject(provisionalCalendarInputDTO.getSubject());
                    existingProvisionalCalendarInput.setInputText(provisionalCalendarInputDTO.getInputText());
                    existingProvisionalCalendarInput.setFootnoteText(provisionalCalendarInputDTO.getFootnoteText());
                    existingProvisionalCalendarInput.setApproverName(provisionalCalendarInputDTO.getApproverName());
                    existingProvisionalCalendarInput.setApproverDesignation(provisionalCalendarInputDTO.getApproverDesignation());

                    ProvisionalCalendarInput savedProvisionalCalendarInput = provisionalCalendarInputRepository.save(existingProvisionalCalendarInput);
                    return mapToDto(savedProvisionalCalendarInput);
                }).orElseThrow(() -> new RuntimeException("ProvisionalCalendarInput not found with id " + id));
    }

    @Override
    public void deleteProvisionalCalendarInput(Long id) {
        if (provisionalCalendarInputRepository.existsById(id)) {
            provisionalCalendarInputRepository.deleteById(id);
        } else {
            throw new RuntimeException("ProvisionalCalendarInput not found with id " + id);
        }
    }

    private ProvisionalCalendarInputDTO mapToDto(ProvisionalCalendarInput provisionalCalendarInput) {
        return ProvisionalCalendarInputDTO.builder()
                .id(provisionalCalendarInput.getId())
                .lokSabhaNo(provisionalCalendarInput.getLokSabhaNo())
                .sessionNo(provisionalCalendarInput.getSessionNo())
                .dateOfInput(provisionalCalendarInput.getDateOfInput())
                .language(provisionalCalendarInput.getLanguage())
                .submittedBy(provisionalCalendarInput.getSubmittedBy())
                .submittedByBranch(provisionalCalendarInput.getSubmittedByBranch())
                .subject(provisionalCalendarInput.getSubject())
                .inputText(provisionalCalendarInput.getInputText())
                .footnoteText(provisionalCalendarInput.getFootnoteText())
                .approverName(provisionalCalendarInput.getApproverName())
                .approverDesignation(provisionalCalendarInput.getApproverDesignation())
                .build();
    }


}
