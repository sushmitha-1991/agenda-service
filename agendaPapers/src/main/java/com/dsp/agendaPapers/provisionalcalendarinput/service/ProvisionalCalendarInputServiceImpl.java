package com.dsp.agendaPapers.provisionalcalendarinput.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dsp.agendaPapers.provisionalcalendarinput.entity.ProvisionalCalendarInputTest;
import com.dsp.agendaPapers.provisionalcalendarinput.mapper.ProvisionalCalendarInputMapper;
import com.dsp.agendaPapers.repository.ApplicationInstanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsp.agendaPapers.provisionalcalendarinput.Dto.ProvisionalCalendarInputDTO;


import com.dsp.agendaPapers.provisionalcalendarinput.repository.ProvisionalCalendarInputRepository;

@Service
public class ProvisionalCalendarInputServiceImpl implements ProvisionalCalendarInputService {

	@Autowired
	ProvisionalCalendarInputRepository provisionalCalendarInputRepository;

	@Autowired
	ApplicationInstanceRepository applicationInstanceRepository;

	@Transactional
	public ProvisionalCalendarInputDTO createProvisionalCalendarInput(
			ProvisionalCalendarInputDTO provisionalCalendarInputDTO) {

		// Build and save ProvisionalCalendarInputTest entity
		ProvisionalCalendarInputTest provisionalCalendarInput = ProvisionalCalendarInputTest.builder()
				.dateOfInput(provisionalCalendarInputDTO.getDateOfInput())
				.language(provisionalCalendarInputDTO.getLanguage())
				.submittedBy(provisionalCalendarInputDTO.getSubmittedBy())
				.submittedByBranch(provisionalCalendarInputDTO.getSubmittedByBranch())
				.subject(provisionalCalendarInputDTO.getSubject())
				.inputText(provisionalCalendarInputDTO.getInputText())
				.footnoteText(provisionalCalendarInputDTO.getFootnoteText())
				.approverName(provisionalCalendarInputDTO.getApproverName())
				.approverDesignation(provisionalCalendarInputDTO.getApproverDesignation())
				.dateOfSittingInputs(provisionalCalendarInputDTO.getDateOfSittingInputs())
				.listOfDocument(provisionalCalendarInputDTO.getListOfDocument())
				.draftStatus(provisionalCalendarInputDTO.getDraftStatus())
				.content(provisionalCalendarInputDTO.getContent())
				.generatedFlag(provisionalCalendarInputDTO.getGeneratedFlag())
				.lokSabhaSessionNumber(provisionalCalendarInputDTO.getLokSabhaNo()) // ApplicationInstance fields
				.lokSabhaSession(provisionalCalendarInputDTO.getSessionNo())
				.currentStatus(provisionalCalendarInputDTO.getStatus())
				.build();

		ProvisionalCalendarInputTest savedProvisionalCalendarInput =
				provisionalCalendarInputRepository.save(provisionalCalendarInput);

		return mapToDto(savedProvisionalCalendarInput);
	}


     // get by Id is working fine

	@Override
	public ProvisionalCalendarInputDTO getProvisionalCalendarInputById(Long id) {
		Optional<ProvisionalCalendarInputTest> provisionalCalendarInputOpt = provisionalCalendarInputRepository
				.findById(id);
		return provisionalCalendarInputOpt.map(this::mapToDto).orElse(null);
	}

	// Get All is Also working

	@Override
	public List<ProvisionalCalendarInputDTO> getAllProvisionalCalendarInputs() {
		List<ProvisionalCalendarInputTest> provisionalCalendarInputs = provisionalCalendarInputRepository.findAll();
		return provisionalCalendarInputs.stream().map(this::mapToDto).collect(Collectors.toList());
	}


// update is working with Application Instance

	@Override
	public ProvisionalCalendarInputDTO updateProvisionalCalendarInput(Long id, ProvisionalCalendarInputDTO provisionalCalendarInputDTO) {
		Optional<ProvisionalCalendarInputTest> existingEntityOpt = provisionalCalendarInputRepository.findById(id);

		if (existingEntityOpt.isPresent()) {
			ProvisionalCalendarInputTest existingEntity = existingEntityOpt.get();

			// Update fields specific to ProvisionalCalendarInputTest
			existingEntity.setDateOfInput(provisionalCalendarInputDTO.getDateOfInput());
			existingEntity.setLanguage(provisionalCalendarInputDTO.getLanguage());
			existingEntity.setSubmittedBy(provisionalCalendarInputDTO.getSubmittedBy());
			existingEntity.setSubmittedByBranch(provisionalCalendarInputDTO.getSubmittedByBranch());
			existingEntity.setSubject(provisionalCalendarInputDTO.getSubject());
			existingEntity.setInputText(provisionalCalendarInputDTO.getInputText());
			existingEntity.setFootnoteText(provisionalCalendarInputDTO.getFootnoteText());
			existingEntity.setApproverName(provisionalCalendarInputDTO.getApproverName());
			existingEntity.setApproverDesignation(provisionalCalendarInputDTO.getApproverDesignation());
			existingEntity.setDateOfSittingInputs(provisionalCalendarInputDTO.getDateOfSittingInputs());
			existingEntity.setListOfDocument(provisionalCalendarInputDTO.getListOfDocument());
			existingEntity.setDraftStatus(provisionalCalendarInputDTO.getDraftStatus());
			existingEntity.setContent(provisionalCalendarInputDTO.getContent());
			existingEntity.setGeneratedFlag(provisionalCalendarInputDTO.getGeneratedFlag());

			// Update ApplicationInstance-related fields
			existingEntity.setLokSabhaSessionNumber(provisionalCalendarInputDTO.getLokSabhaNo());
			existingEntity.setLokSabhaSession(provisionalCalendarInputDTO.getSessionNo());
			existingEntity.setCurrentStatus(provisionalCalendarInputDTO.getStatus());

			// Save the updated entity
			ProvisionalCalendarInputTest updatedProvisionalCalendarInput = provisionalCalendarInputRepository.save(existingEntity);

			// Map to DTO and return
			return mapToDto(updatedProvisionalCalendarInput);
		}

		return null; // Return null if the entity with the given ID is not found
	}




	@Override
	public void deleteProvisionalCalendarInput(Long id) {
		provisionalCalendarInputRepository.deleteById(id);
	}

	// Save ProvisionalCalendarInput with specified draft status
	@Override
	public ProvisionalCalendarInputDTO saveProvisionalCalendarInput(ProvisionalCalendarInputDTO dto,
			Boolean draftStatus) {
		ProvisionalCalendarInputTest provisionalCalendarInput = ProvisionalCalendarInputMapper.toEntity(dto);
		provisionalCalendarInput.setDraftStatus(draftStatus);
		ProvisionalCalendarInputTest savedProvisionalCalendarInput = provisionalCalendarInputRepository
				.save(provisionalCalendarInput);
		return mapToDto(savedProvisionalCalendarInput);
	}

	@Override
	public List<ProvisionalCalendarInputDTO> getProvisionalData(String submittedByBranch, String status, Date startDate, Date endDate) {
		return List.of();
	}

	@Override
	public List<ProvisionalCalendarInputDTO> getProvisionalInputsByStatus(List<String> statuses) {
		return List.of();
	}



	// Helper method to map entity to DTO
	private ProvisionalCalendarInputDTO mapToDto(ProvisionalCalendarInputTest provisionalCalendarInput) {
		return ProvisionalCalendarInputDTO.builder().id(provisionalCalendarInput.getId())
				.dateOfInput(provisionalCalendarInput.getDateOfInput()).language(provisionalCalendarInput.getLanguage())
				.submittedBy(provisionalCalendarInput.getSubmittedBy())
				.submittedByBranch(provisionalCalendarInput.getSubmittedByBranch())
				.subject(provisionalCalendarInput.getSubject()).inputText(provisionalCalendarInput.getInputText())
				.footnoteText(provisionalCalendarInput.getFootnoteText())
				.approverName(provisionalCalendarInput.getApproverName())
				.approverDesignation(provisionalCalendarInput.getApproverDesignation())
				.dateOfSittingInputs(provisionalCalendarInput.getDateOfSittingInputs())
				.listOfDocument(provisionalCalendarInput.getListOfDocument())
				.draftStatus(provisionalCalendarInput.getDraftStatus())
				.content(provisionalCalendarInput.getContent())
				.generatedFlag(provisionalCalendarInput.getGeneratedFlag())
				.build();
	}}
//
////	// Helper method to map DTO to Entity
//	private ProvisionalCalendarInputTest mapToEntity(ProvisionalCalendarInputDTO dto) {
//		return ProvisionalCalendarInputTest.builder().lokSabhaNo(dto.getLokSabhaNo()).sessionNo(dto.getSessionNo())
//				.dateOfInput(dto.getDateOfInput()).language(dto.getLanguage()).submittedBy(dto.getSubmittedBy())
//				.submittedByBranch(dto.getSubmittedByBranch()).subject(dto.getSubject()).inputText(dto.getInputText())
//				.footnoteText(dto.getFootnoteText()).approverName(dto.getApproverName())
//				.approverDesignation(dto.getApproverDesignation()).dateOfSittingInputs(dto.getDateOfSittingInputs())
//				.status(dto.getStatus()).listOfDocument(dto.getListOfDocument()).draftStatus(dto.getDraftStatus())
//				.content(dto.getContent())
//				.generatedFlag(dto.getGeneratedFlag())
//				.build();
//	}
