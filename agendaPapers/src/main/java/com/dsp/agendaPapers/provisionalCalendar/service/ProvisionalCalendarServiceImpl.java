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
                .branchId(provisionalCalendarDTO.getBranchId())
                .provisionalCalendarDate(provisionalCalendarDTO.getProvisionalCalendarDate())
                .applicationNo(provisionalCalendarDTO.getApplicationNo())
                .status(provisionalCalendarDTO.getStatus())
                .note(provisionalCalendarDTO.getNote())
                .footNote(provisionalCalendarDTO.getFootNote())
                .approverName(provisionalCalendarDTO.getApproverName())
                .approverDesignation(provisionalCalendarDTO.getApproverDesignation())
                .parentId(provisionalCalendarDTO.getParentId())
                .additionalDetails(provisionalCalendarDTO.getAdditionalDetails())
                .balloutsDetails(provisionalCalendarDTO.getBalloutsDetails())
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
                    existingProvisionalCalendar.setBranchId(provisionalCalendarDTO.getBranchId());
                    existingProvisionalCalendar.setProvisionalCalendarDate(provisionalCalendarDTO.getProvisionalCalendarDate());
                    existingProvisionalCalendar.setApplicationNo(provisionalCalendarDTO.getApplicationNo());
                    existingProvisionalCalendar.setStatus(provisionalCalendarDTO.getStatus());
                    existingProvisionalCalendar.setNote(provisionalCalendarDTO.getNote());
                    existingProvisionalCalendar.setFootNote(provisionalCalendarDTO.getFootNote());
                    existingProvisionalCalendar.setApproverName(provisionalCalendarDTO.getApproverName());
                    existingProvisionalCalendar.setApproverDesignation(provisionalCalendarDTO.getApproverDesignation());
                    existingProvisionalCalendar.setParentId(provisionalCalendarDTO.getParentId());
                    existingProvisionalCalendar.setAdditionalDetails(provisionalCalendarDTO.getAdditionalDetails());
                    existingProvisionalCalendar.setBalloutsDetails(provisionalCalendarDTO.getBalloutsDetails());

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

    private ProvisionalCalendarDTO mapToDto(ProvisionalCalendar provisionalCalendar) {
        return ProvisionalCalendarDTO.builder()
                .id(provisionalCalendar.getId())
                .lokSabhaNo(provisionalCalendar.getLokSabhaNo())
                .sessionNo(provisionalCalendar.getSessionNo())
                .branchId(provisionalCalendar.getBranchId())
                .provisionalCalendarDate(provisionalCalendar.getProvisionalCalendarDate())
                .applicationNo(provisionalCalendar.getApplicationNo())
                .status(provisionalCalendar.getStatus())
                .note(provisionalCalendar.getNote())
                .footNote(provisionalCalendar.getFootNote())
                .approverName(provisionalCalendar.getApproverName())
                .approverDesignation(provisionalCalendar.getApproverDesignation())
                .parentId(provisionalCalendar.getParentId())
                .additionalDetails(provisionalCalendar.getAdditionalDetails())
                .balloutsDetails(provisionalCalendar.getBalloutsDetails())
                .build();
    }

}
