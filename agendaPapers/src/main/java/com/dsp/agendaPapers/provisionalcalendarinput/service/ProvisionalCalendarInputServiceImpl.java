package com.dsp.agendaPapers.provisionalcalendarinput.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsp.agendaPapers.provisionalcalendarinput.Dto.ProvisionalCalendarInputDTO;
import com.dsp.agendaPapers.provisionalcalendarinput.entity.ProvisionalCalendarInput;
import com.dsp.agendaPapers.provisionalcalendarinput.mapper.ProvisionalCalendarInputMapper;
import com.dsp.agendaPapers.provisionalcalendarinput.repository.ProvisionalCalendarInputRepository;

@Service
public class ProvisionalCalendarInputServiceImpl implements ProvisionalCalendarInputService {

	@Autowired
	ProvisionalCalendarInputRepository provisionalCalendarInputRepository;

	@Override
	public ProvisionalCalendarInputDTO createProvisionalCalendarInput(
			ProvisionalCalendarInputDTO provisionalCalendarInputDTO) {
		ProvisionalCalendarInput provisionalCalendarInput = ProvisionalCalendarInput.builder()
				.lokSabhaNo(provisionalCalendarInputDTO.getLokSabhaNo())
				.sessionNo(provisionalCalendarInputDTO.getSessionNo())
				.dateOfInput(provisionalCalendarInputDTO.getDateOfInput())
				.language(provisionalCalendarInputDTO.getLanguage())
				.submittedBy(provisionalCalendarInputDTO.getSubmittedBy())
				.submittedByBranch(provisionalCalendarInputDTO.getSubmittedByBranch())
				.subject(provisionalCalendarInputDTO.getSubject()).inputText(provisionalCalendarInputDTO.getInputText())
				.footnoteText(provisionalCalendarInputDTO.getFootnoteText())
				.approverName(provisionalCalendarInputDTO.getApproverName())
				.approverDesignation(provisionalCalendarInputDTO.getApproverDesignation())
				.dateOfSittingInputs(provisionalCalendarInputDTO.getDateOfSittingInputs()) // List<Map<String, Object>>
				.status(provisionalCalendarInputDTO.getStatus())
				.listOfDocument(provisionalCalendarInputDTO.getListOfDocument()) // List<Map<String, Object>>
				.draftStatus(provisionalCalendarInputDTO.getDraftStatus())
				.content(provisionalCalendarInputDTO.getContent())
				.build();

		ProvisionalCalendarInput savedProvisionalCalendarInput = provisionalCalendarInputRepository
				.save(provisionalCalendarInput);
		return mapToDto(savedProvisionalCalendarInput);
	}

	@Override
	public ProvisionalCalendarInputDTO getProvisionalCalendarInputById(Long id) {
		Optional<ProvisionalCalendarInput> provisionalCalendarInputOpt = provisionalCalendarInputRepository
				.findById(id);
		return provisionalCalendarInputOpt.map(this::mapToDto).orElse(null);
	}

	@Override
	public List<ProvisionalCalendarInputDTO> getAllProvisionalCalendarInputs() {
		List<ProvisionalCalendarInput> provisionalCalendarInputs = provisionalCalendarInputRepository.findAll();
		return provisionalCalendarInputs.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public ProvisionalCalendarInputDTO updateProvisionalCalendarInput(Long id,
			ProvisionalCalendarInputDTO provisionalCalendarInputDTO) {
		Optional<ProvisionalCalendarInput> existingEntityOpt = provisionalCalendarInputRepository.findById(id);
		if (existingEntityOpt.isPresent()) {
			ProvisionalCalendarInput existingEntity = existingEntityOpt.get();
			// Update fields
			existingEntity.setLokSabhaNo(provisionalCalendarInputDTO.getLokSabhaNo());
			existingEntity.setSessionNo(provisionalCalendarInputDTO.getSessionNo());
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
			existingEntity.setStatus(provisionalCalendarInputDTO.getStatus());
			existingEntity.setListOfDocument(provisionalCalendarInputDTO.getListOfDocument());
			existingEntity.setDraftStatus(provisionalCalendarInputDTO.getDraftStatus());
			existingEntity.setContent(provisionalCalendarInputDTO.getContent());

			ProvisionalCalendarInput updatedProvisionalCalendarInput = provisionalCalendarInputRepository
					.save(existingEntity);
			return mapToDto(updatedProvisionalCalendarInput);
		}
		return null;
	}

	@Override
	public void deleteProvisionalCalendarInput(Long id) {
		provisionalCalendarInputRepository.deleteById(id);
	}

	// Save ProvisionalCalendarInput with specified draft status
	@Override
	public ProvisionalCalendarInputDTO saveProvisionalCalendarInput(ProvisionalCalendarInputDTO dto,
			Boolean draftStatus) {
		ProvisionalCalendarInput provisionalCalendarInput = ProvisionalCalendarInputMapper.toEntity(dto);
		provisionalCalendarInput.setDraftStatus(draftStatus);
		ProvisionalCalendarInput savedProvisionalCalendarInput = provisionalCalendarInputRepository
				.save(provisionalCalendarInput);
		return mapToDto(savedProvisionalCalendarInput);
	}

	// Helper method to map entity to DTO
	private ProvisionalCalendarInputDTO mapToDto(ProvisionalCalendarInput provisionalCalendarInput) {
		return ProvisionalCalendarInputDTO.builder().id(provisionalCalendarInput.getId())
				.lokSabhaNo(provisionalCalendarInput.getLokSabhaNo()).sessionNo(provisionalCalendarInput.getSessionNo())
				.dateOfInput(provisionalCalendarInput.getDateOfInput()).language(provisionalCalendarInput.getLanguage())
				.submittedBy(provisionalCalendarInput.getSubmittedBy())
				.submittedByBranch(provisionalCalendarInput.getSubmittedByBranch())
				.subject(provisionalCalendarInput.getSubject()).inputText(provisionalCalendarInput.getInputText())
				.footnoteText(provisionalCalendarInput.getFootnoteText())
				.approverName(provisionalCalendarInput.getApproverName())
				.approverDesignation(provisionalCalendarInput.getApproverDesignation())
				.dateOfSittingInputs(provisionalCalendarInput.getDateOfSittingInputs())
				.status(provisionalCalendarInput.getStatus())
				.listOfDocument(provisionalCalendarInput.getListOfDocument())
				.draftStatus(provisionalCalendarInput.getDraftStatus())
				.content(provisionalCalendarInput.getContent())
				.build();
	}

	// Helper method to map DTO to Entity
	private ProvisionalCalendarInput mapToEntity(ProvisionalCalendarInputDTO dto) {
		return ProvisionalCalendarInput.builder().lokSabhaNo(dto.getLokSabhaNo()).sessionNo(dto.getSessionNo())
				.dateOfInput(dto.getDateOfInput()).language(dto.getLanguage()).submittedBy(dto.getSubmittedBy())
				.submittedByBranch(dto.getSubmittedByBranch()).subject(dto.getSubject()).inputText(dto.getInputText())
				.footnoteText(dto.getFootnoteText()).approverName(dto.getApproverName())
				.approverDesignation(dto.getApproverDesignation()).dateOfSittingInputs(dto.getDateOfSittingInputs())
				.status(dto.getStatus()).listOfDocument(dto.getListOfDocument()).draftStatus(dto.getDraftStatus())
				.content(dto.getContent())
				.build();
	}
}