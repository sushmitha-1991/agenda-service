package com.dsp.agendaPapers.provisionalCalendar.service;

import com.dsp.agendaPapers.provisionalCalendar.Dto.ProvisionalCalendarDTO;
import com.dsp.agendaPapers.provisionalCalendar.entity.ProvisionalCalendar;
import com.dsp.agendaPapers.provisionalCalendar.repository.ProvisionalCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProvisionalCalendarServiceImpl implements ProvisionalCalendarService{

    @Autowired
    private ProvisionalCalendarRepository provisionalCalendarRepository;


    @Override
    public ProvisionalCalendarDTO createProvisionalCalendar(ProvisionalCalendarDTO provisionalCalendarDTO) {
        ProvisionalCalendar provisionalCalendar = ProvisionalCalendar.builder()
                .lokSabhaNo(provisionalCalendarDTO.getLokSabhaNo())
                .sessionNo(provisionalCalendarDTO.getSessionNo())
                .rajyaSabhaSessionNo(provisionalCalendarDTO.getRajyaSabhaSessionNo())  // New field
                .content(provisionalCalendarDTO.getContent())  // New field
                .generatedDocId(provisionalCalendarDTO.getGeneratedDocId())  // New field
                .draftStatus(provisionalCalendarDTO.getDraftStatus())
                .build();

        ProvisionalCalendar savedProvisionalCalendar = provisionalCalendarRepository.save(provisionalCalendar);

        return mapToDto(savedProvisionalCalendar);
    }

    @Override
    public ProvisionalCalendarDTO getProvisionalCalendarById(Long id) {
        ProvisionalCalendar provisionalCalendar = provisionalCalendarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProvisionalCalendar not found with the given ID"));
        return mapToDto(provisionalCalendar);
    }

    @Override
    public List<ProvisionalCalendarDTO> getAllProvisionalCalendars() {
        return provisionalCalendarRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProvisionalCalendarDTO updateProvisionalCalendar(Long id, ProvisionalCalendarDTO provisionalCalendarDTO) {
        return provisionalCalendarRepository.findById(id)
                .map(existingProvisionalCalendar -> {
                    existingProvisionalCalendar.setLokSabhaNo(provisionalCalendarDTO.getLokSabhaNo());
                    existingProvisionalCalendar.setSessionNo(provisionalCalendarDTO.getSessionNo());
                    existingProvisionalCalendar.setRajyaSabhaSessionNo(provisionalCalendarDTO.getRajyaSabhaSessionNo());  // New field
                    existingProvisionalCalendar.setContent(provisionalCalendarDTO.getContent());  // New field
                    existingProvisionalCalendar.setGeneratedDocId(provisionalCalendarDTO.getGeneratedDocId());  // New field
                    existingProvisionalCalendar.setDraftStatus(provisionalCalendarDTO.getDraftStatus());  // New field

                    ProvisionalCalendar savedProvisionalCalendar = provisionalCalendarRepository.save(existingProvisionalCalendar);
                    return mapToDto(savedProvisionalCalendar);
                }).orElseThrow(() -> new RuntimeException("ProvisionalCalendar not found with id " + id));
    }

    @Override
    public void deleteProvisionalCalendar(Long id) {
        if (provisionalCalendarRepository.existsById(id)) {
            provisionalCalendarRepository.deleteById(id);
        } else {
            throw new RuntimeException("ProvisionalCalendar not found with id " + id);
        }
    }

    @Override
    public List<ProvisionalCalendarDTO> getProvisionalCalendarsByLokSabhaNo(String lokSabhaNo) {
        return provisionalCalendarRepository.findByLokSabhaNo(lokSabhaNo)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProvisionalCalendarDTO> getProvisionalCalendarsBySessionNo(String sessionNo) {
        return provisionalCalendarRepository.findBySessionNo(sessionNo)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProvisionalCalendarDTO> getProvisionalCalendarsByRajyaSabhaSessionNo(String rajyaSabhaSessionNo) {
        return provisionalCalendarRepository.findByRajyaSabhaSessionNo(rajyaSabhaSessionNo)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProvisionalCalendarDTO> getProvisionalCalendarsByDraftStatus(Boolean draftStatus) {
        return provisionalCalendarRepository.findByDraftStatus(draftStatus)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ProvisionalCalendarDTO mapToDto(ProvisionalCalendar provisionalCalendar) {
        return ProvisionalCalendarDTO.builder()
                .id(provisionalCalendar.getId())
                .lokSabhaNo(provisionalCalendar.getLokSabhaNo())
                .sessionNo(provisionalCalendar.getSessionNo())
                .rajyaSabhaSessionNo(provisionalCalendar.getRajyaSabhaSessionNo())  // New field
                .content(provisionalCalendar.getContent())  // New field
                .generatedDocId(provisionalCalendar.getGeneratedDocId())  // New field
                .draftStatus(provisionalCalendar.getDraftStatus())  // New field
                .build();
    }

}
