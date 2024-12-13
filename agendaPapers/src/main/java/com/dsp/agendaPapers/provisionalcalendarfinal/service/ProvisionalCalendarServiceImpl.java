package com.dsp.agendaPapers.provisionalcalendarfinal.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsp.agendaPapers.dto.ApplicationInstanceDTO;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarDTO;
//import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarGenerateDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarGenerateDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarInputDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarTableOfficeDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.entity.ProvisionalCalendar;
import com.dsp.agendaPapers.provisionalcalendarfinal.mapper.ProvisionalCalendarDTOMapper;
import com.dsp.agendaPapers.provisionalcalendarfinal.repository.ProvisionalCalendarRepository;
import com.dsp.agendaPapers.repository.ApplicationInstanceRepository;

import jakarta.transaction.Transactional;

@Service
public class ProvisionalCalendarServiceImpl implements ProvisionalCalendarService {

	@Autowired
	private ProvisionalCalendarRepository provisionalCalendarRepository;

	@Autowired
	ApplicationInstanceRepository applicationInstanceRepository;

	@Transactional
	@Override
	public ProvisionalCalendarDTO createProvisionalCalendar(ProvisionalCalendarDTO provisionalCalendarDTO) {
		ProvisionalCalendar provisionalCalendar = ProvisionalCalendar.builder()
				.dateOfInput(provisionalCalendarDTO.getDateOfInput()).language(provisionalCalendarDTO.getLanguage())
				.submittedBy(provisionalCalendarDTO.getSubmittedBy())
				.submittedByBranch(provisionalCalendarDTO.getSubmittedByBranch())
				.subject(provisionalCalendarDTO.getSubject()).inputText(provisionalCalendarDTO.getInputText())
				.footnoteText(provisionalCalendarDTO.getFootnoteText())
				.approverName(provisionalCalendarDTO.getApproverName())
				.approverDesignation(provisionalCalendarDTO.getApproverDesignation())
				.dateOfSittingInputs(provisionalCalendarDTO.getDateOfSittingInputs())
				.listOfDocument(provisionalCalendarDTO.getListOfDocument())
				.groupchartsLS(provisionalCalendarDTO.getGroupchartsLS())
				.groupchartsRS(provisionalCalendarDTO.getGroupchartsRS())
				.listOfDocumentTableOffice(provisionalCalendarDTO.getListOfDocumentTableOffice())
				.listOfDocumentTableOfficeFootnote(provisionalCalendarDTO.getListOfDocumentTableOfficeFootnote())
				.listOfDocumentTableOfficeNote(provisionalCalendarDTO.getListOfDocumentTableOfficeNote())
				.dateOfSittingInputsTableOffice(provisionalCalendarDTO.getDateOfSittingInputsTableOffice())
				.dateOfInputTableOffice(provisionalCalendarDTO.getDateOfInputTableOffice())
				.inputTextTableOffice(provisionalCalendarDTO.getInputTextTableOffice())
				.rajyaSabhaSession(provisionalCalendarDTO.getRajyaSabhaSessionNo())
				.generatedFlag(provisionalCalendarDTO.getGeneratedFlag()).content(provisionalCalendarDTO.getContent())
				// application instance
				.id(provisionalCalendarDTO.getId()).applicationNo(provisionalCalendarDTO.getApplicationNo())
				.currentStatus(provisionalCalendarDTO.getCurrentStatus())
				.publishedDocumentUrl(provisionalCalendarDTO.getPublishedDocumentUrl())
//                .processInstanceId(provisionalCalendarDTO.getProcessInstanceId())
				.isPublished(provisionalCalendarDTO.getIsPublished())
				.lokSabhaSession(provisionalCalendarDTO.getLokSabhaSession())
				.lokSabhaSessionNumber(provisionalCalendarDTO.getLokSabhaSessionNumber())
				.rajyaSabhaSession(provisionalCalendarDTO.getRajyaSabhaSession())
//                .otherInfo((Map<String, Object>) questionbranchInputDTO.getOtherInfo())
				.applicationId(provisionalCalendarDTO.getApplicationId()).draft(false).build();

		ProvisionalCalendar savedEntity = provisionalCalendarRepository.save(provisionalCalendar);
		return mapToDto(savedEntity);
	}

	@Override
	public ProvisionalCalendarDTO getProvisionalCalendarById(Long id) {
		ProvisionalCalendar provisionalCalendar = provisionalCalendarRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("ProvisionalCalendar not found with id " + id));
		return mapToDto(provisionalCalendar);
	}

	@Override
	public List<ProvisionalCalendarDTO> getAllProvisionalCalendars() {
		return provisionalCalendarRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	@Override
	public ProvisionalCalendarDTO updateProvisionalCalendar(Long id, ProvisionalCalendarDTO provisionalCalendarDTO) {
		return provisionalCalendarRepository.findById(id).map(existingEntity -> {
			existingEntity.setDateOfInput(provisionalCalendarDTO.getDateOfInput());
			existingEntity.setLanguage(provisionalCalendarDTO.getLanguage());
			existingEntity.setSubmittedBy(provisionalCalendarDTO.getSubmittedBy());
			existingEntity.setSubmittedByBranch(provisionalCalendarDTO.getSubmittedByBranch());
			existingEntity.setSubject(provisionalCalendarDTO.getSubject());
			existingEntity.setInputText(provisionalCalendarDTO.getInputText());
			existingEntity.setFootnoteText(provisionalCalendarDTO.getFootnoteText());
			existingEntity.setApproverName(provisionalCalendarDTO.getApproverName());
			existingEntity.setApproverDesignation(provisionalCalendarDTO.getApproverDesignation());
			existingEntity.setDateOfSittingInputs(provisionalCalendarDTO.getDateOfSittingInputs());
			existingEntity.setListOfDocument(provisionalCalendarDTO.getListOfDocument());
			existingEntity.setGroupchartsLS(provisionalCalendarDTO.getGroupchartsLS());
			existingEntity.setGroupchartsRS(provisionalCalendarDTO.getGroupchartsRS());
			existingEntity.setListOfDocumentTableOffice(provisionalCalendarDTO.getListOfDocumentTableOffice());
			existingEntity.setListOfDocumentTableOfficeFootnote(
					provisionalCalendarDTO.getListOfDocumentTableOfficeFootnote());
			existingEntity.setListOfDocumentTableOfficeNote(provisionalCalendarDTO.getListOfDocumentTableOfficeNote());
			existingEntity
					.setDateOfSittingInputsTableOffice(provisionalCalendarDTO.getDateOfSittingInputsTableOffice());
			existingEntity.setDateOfInputTableOffice(provisionalCalendarDTO.getDateOfInputTableOffice());
			existingEntity.setInputTextTableOffice(provisionalCalendarDTO.getInputTextTableOffice());
			existingEntity.setRajyaSabhaSession(provisionalCalendarDTO.getRajyaSabhaSessionNo());
			existingEntity.setGeneratedFlag(provisionalCalendarDTO.getGeneratedFlag());
			existingEntity.setContent(provisionalCalendarDTO.getContent());
			// adding the new field inputstaus
			existingEntity.setInputStatus(provisionalCalendarDTO.getInputStatus());

			ProvisionalCalendar updatedEntity = provisionalCalendarRepository.save(existingEntity);
			return mapToDto(updatedEntity);
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
	public void softDeleteProvisionalCalendar(Long id) {

	}

	@Override
	public ProvisionalCalendarInputDTO createProvisionalCalendarInput(ProvisionalCalendarInputDTO inputDTO) {
		// Map DTO to Entity
		String applicationNo = generateApplicationNo(inputDTO.getLokSabhaSession(),
				inputDTO.getLokSabhaSessionNumber());

		ProvisionalCalendar provisionalCalendar = ProvisionalCalendar.builder().dateOfInput(inputDTO.getDateOfInput())
				.language(inputDTO.getLanguage()).submittedBy(inputDTO.getSubmittedBy())
				.submittedByBranch(inputDTO.getSubmittedByBranch()).subject(inputDTO.getSubject())
				.inputText(inputDTO.getInputText()).footnoteText(inputDTO.getFootnoteText())
				.approverName(inputDTO.getApproverName()).approverDesignation(inputDTO.getApproverDesignation())
				.dateOfSittingInputs(inputDTO.getDateOfSittingInputs()).listOfDocument(inputDTO.getListOfDocument())
				.applicationId(inputDTO.getApplicationId()).lokSabhaSession(inputDTO.getLokSabhaSession())
				.lokSabhaSessionNumber(inputDTO.getLokSabhaSessionNumber())
				.rajyaSabhaSession(inputDTO.getRajyaSabhaSession()).applicationNo(applicationNo) // Set applicationNo
				.currentStatus(inputDTO.getStatus()).build();

		ProvisionalCalendar savedEntity = provisionalCalendarRepository.save(provisionalCalendar);

		return mapToDtoPCinput(savedEntity);

	}

	@Override
	public ProvisionalCalendarTableOfficeDTO createProvisionalCalendarTableOffice(
			ProvisionalCalendarTableOfficeDTO provisionalCalendarTableOfficeDTO) {
		// Check if provisionalCalendarInputId exists
		Optional<ProvisionalCalendar> optionalProvisionalCalendar = provisionalCalendarRepository
				.findById(provisionalCalendarTableOfficeDTO.getProvisionalCalendarInputId());

		if (optionalProvisionalCalendar.isEmpty()) {
			throw new ResourceNotFoundException("ProvisionalCalendar with ID "
					+ provisionalCalendarTableOfficeDTO.getProvisionalCalendarInputId() + " not found");
		}

		// Retrieve the existing ProvisionalCalendar entity
		ProvisionalCalendar provisionalCalendar = optionalProvisionalCalendar.get();

		// Update the fields with values from the DTO
		provisionalCalendar.setGroupchartsLS(provisionalCalendarTableOfficeDTO.getGroupchartsLS());
		provisionalCalendar.setGroupchartsRS(provisionalCalendarTableOfficeDTO.getGroupchartsRS());
		provisionalCalendar
				.setListOfDocumentTableOffice(provisionalCalendarTableOfficeDTO.getListOfDocumentTableOffice());
		provisionalCalendar.setListOfDocumentTableOfficeFootnote(
				provisionalCalendarTableOfficeDTO.getListOfDocumentTableOfficeFootnote());
		provisionalCalendar
				.setListOfDocumentTableOfficeNote(provisionalCalendarTableOfficeDTO.getListOfDocumentTableOfficeNote());
		provisionalCalendar.setDateOfSittingInputsTableOffice(
				provisionalCalendarTableOfficeDTO.getDateOfSittingInputsTableOffice());
		provisionalCalendar.setDateOfInputTableOffice(provisionalCalendarTableOfficeDTO.getDateOfInputTableOffice());
		provisionalCalendar.setInputTextTableOffice(provisionalCalendarTableOfficeDTO.getInputTextTableOffice());
		provisionalCalendar.setGeneratedFlag(provisionalCalendarTableOfficeDTO.getGeneratedFlag());

		// Save the updated entity
		ProvisionalCalendar updatedProvisionalCalendar = provisionalCalendarRepository.save(provisionalCalendar);

		// Map the updated entity back to a DTO
		// Map the updated entity back to a ProvisionalCalendarTableOfficeDTO
		return ProvisionalCalendarTableOfficeDTO.builder().id(updatedProvisionalCalendar.getId())
				.groupchartsLS(updatedProvisionalCalendar.getGroupchartsLS())
				.groupchartsRS(updatedProvisionalCalendar.getGroupchartsRS())
				.listOfDocumentTableOffice(updatedProvisionalCalendar.getListOfDocumentTableOffice())
				.listOfDocumentTableOfficeFootnote(updatedProvisionalCalendar.getListOfDocumentTableOfficeFootnote())
				.listOfDocumentTableOfficeNote(updatedProvisionalCalendar.getListOfDocumentTableOfficeNote())
				.dateOfSittingInputsTableOffice(updatedProvisionalCalendar.getDateOfSittingInputsTableOffice())
				.dateOfInputTableOffice(updatedProvisionalCalendar.getDateOfInputTableOffice())
				.inputTextTableOffice(updatedProvisionalCalendar.getInputTextTableOffice())
				.generatedFlag(updatedProvisionalCalendar.getGeneratedFlag())
				.provisionalCalendarInputId(provisionalCalendarTableOfficeDTO.getProvisionalCalendarInputId()).build();
	}

	private String generateApplicationNo(String lokSabhaNo, String sessionNo) {
		return "PC" + lokSabhaNo + sessionNo;
	}

	private ProvisionalCalendarDTO mapToDto(ProvisionalCalendar provisionalCalendar) {
		return ProvisionalCalendarDTO.builder().id(provisionalCalendar.getId())
				.dateOfInput(provisionalCalendar.getDateOfInput()).language(provisionalCalendar.getLanguage())
				.submittedBy(provisionalCalendar.getSubmittedBy())
				.submittedByBranch(provisionalCalendar.getSubmittedByBranch()).subject(provisionalCalendar.getSubject())
				.inputText(provisionalCalendar.getInputText()).footnoteText(provisionalCalendar.getFootnoteText())
				.approverName(provisionalCalendar.getApproverName())
				.approverDesignation(provisionalCalendar.getApproverDesignation())
				.dateOfSittingInputs(provisionalCalendar.getDateOfSittingInputs())
				.listOfDocument(provisionalCalendar.getListOfDocument())
				.groupchartsLS(provisionalCalendar.getGroupchartsLS())
				.groupchartsRS(provisionalCalendar.getGroupchartsRS())
				.listOfDocumentTableOffice(provisionalCalendar.getListOfDocumentTableOffice())
				.listOfDocumentTableOfficeFootnote(provisionalCalendar.getListOfDocumentTableOfficeFootnote())
				.listOfDocumentTableOfficeNote(provisionalCalendar.getListOfDocumentTableOfficeNote())
				.dateOfSittingInputsTableOffice(provisionalCalendar.getDateOfSittingInputsTableOffice())
				.dateOfInputTableOffice(provisionalCalendar.getDateOfInputTableOffice())
				.inputTextTableOffice(provisionalCalendar.getInputTextTableOffice())
				.rajyaSabhaSessionNo(provisionalCalendar.getRajyaSabhaSession())
				.generatedFlag(provisionalCalendar.getGeneratedFlag()).content(provisionalCalendar.getContent())
				.applicationId(provisionalCalendar.getApplicationId())
				.applicationInstanceDTO(ApplicationInstanceDTO.builder().id(provisionalCalendar.getId())
						.applicationId(provisionalCalendar.getApplicationId())
						.applicationNo(provisionalCalendar.getApplicationNo())
						.currentStatus(provisionalCalendar.getCurrentStatus()).build())
				.build();
	}

//    @Override
//    public ProvisionalCalendarGenerateDTO createProvisionalCalendarGenerate(ProvisionalCalendarGenerateDTO provisionalCalendarGenerateDTO) {
//
//        Optional<ProvisionalCalendar> optionalProvisionalCalendar = provisionalCalendarRepository.findById(provisionalCalendarGenerateDTO.getProvisionalCalendarInputId());
//
//        if (optionalProvisionalCalendar.isEmpty()) {
//            throw new ResourceNotFoundException("ProvisionalCalendar with ID " + provisionalCalendarGenerateDTO.getProvisionalCalendarInputId() + " not found");
//        }
//
//        // Retrieve the existing ProvisionalCalendar entity
//        ProvisionalCalendar provisionalCalendar = optionalProvisionalCalendar.get();
//
//        // Update the fields with values from the DTO
//        provisionalCalendar.setContent(provisionalCalendarGenerateDTO.getContent());
//
//        // Save the updated entity
//        ProvisionalCalendar updatedProvisionalCalendar = provisionalCalendarRepository.save(provisionalCalendar);
//
//        // Map the updated entity back to a DTO
//        // Map the updated entity back to a ProvisionalCalendarTableOfficeDTO
//        return ProvisionalCalendarGenerateDTO.builder()
//                .id(updatedProvisionalCalendar.getId())
//                .content(updatedProvisionalCalendar.getContent())
//
//                .provisionalCalendarInputId(provisionalCalendarGenerateDTO.getProvisionalCalendarInputId())
//                .build();
//    }

	@Override
	public ProvisionalCalendarGenerateDTO createProvisionalCalendarGenerate(
			ProvisionalCalendarGenerateDTO provisionalCalendarGenerateDTO) {
		// Check if ProvisionalCalendar with the given ID exists
		Optional<ProvisionalCalendar> optionalProvisionalCalendar = provisionalCalendarRepository
				.findById(provisionalCalendarGenerateDTO.getProvisionalCalendarInputId());

		if (optionalProvisionalCalendar.isEmpty()) {
			throw new ResourceNotFoundException("ProvisionalCalendar with ID "
					+ provisionalCalendarGenerateDTO.getProvisionalCalendarInputId() + " not found");
		}

		// Retrieve the existing ProvisionalCalendar entity
		ProvisionalCalendar provisionalCalendar = optionalProvisionalCalendar.get();

		// Update the content field with the value from the DTO
		provisionalCalendar.setContent(provisionalCalendarGenerateDTO.getContent());

		// Save the updated entity
		ProvisionalCalendar updatedProvisionalCalendar = provisionalCalendarRepository.save(provisionalCalendar);

		// Map the updated entity back to a ProvisionalCalendarGenerateDTO
		return ProvisionalCalendarGenerateDTO.builder().id(updatedProvisionalCalendar.getId())
				.content(updatedProvisionalCalendar.getContent())
				.provisionalCalendarInputId(provisionalCalendarGenerateDTO.getProvisionalCalendarInputId()).build();
	}

// getAll

	@Override
	public List<ProvisionalCalendarDTO> getAllProvisionalCalendarsBetweenDates(LocalDate startDate, LocalDate endDate) {
		if (startDate == null || endDate == null) {
			// If no dates are provided, fetch all records
			return provisionalCalendarRepository.findAll().stream().map(ProvisionalCalendarDTOMapper::toDTO).toList();
		}

		// Convert LocalDate to LocalDateTime for precise filtering
		LocalDateTime startDateTime = startDate.atStartOfDay();
		LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

		// Fetch records between the date range
		return provisionalCalendarRepository.findByDateOfInputBetween(startDateTime, endDateTime).stream()
				.map(ProvisionalCalendarDTOMapper::toDTO).toList();
	}

	// update for the input status
	@Override
	public ProvisionalCalendarInputDTO updateInputStatus(Long id, String inputStatus) {
		Optional<ProvisionalCalendar> optionalEntity = provisionalCalendarRepository.findById(id);
		if (optionalEntity.isPresent()) {
			ProvisionalCalendar existingEntity = optionalEntity.get();
			existingEntity.setInputStatus(inputStatus); // Ensure the field name is consistent
			ProvisionalCalendar updatedEntity = provisionalCalendarRepository.save(existingEntity);
			return mapToInputDto(updatedEntity);
		} else {
			throw new RuntimeException("ProvisionalCalendar not found with id " + id);
		}
	}

	private ProvisionalCalendarInputDTO mapToInputDto(ProvisionalCalendar entity) {
		return ProvisionalCalendarInputDTO.builder()
				.id(entity.getId())
				.inputStatus(entity.getInputStatus()) // Ensure the field name is consistent
				.build();
	}


	// // for input status





	private ProvisionalCalendarInputDTO mapToDtoPCinput(ProvisionalCalendar provisionalCalendar) {
		return ProvisionalCalendarInputDTO.builder().id(provisionalCalendar.getId())
				.dateOfInput(provisionalCalendar.getDateOfInput()).language(provisionalCalendar.getLanguage())
				.submittedBy(provisionalCalendar.getSubmittedBy())
				.submittedByBranch(provisionalCalendar.getSubmittedByBranch()).subject(provisionalCalendar.getSubject())
				.inputText(provisionalCalendar.getInputText()).footnoteText(provisionalCalendar.getFootnoteText())
				.approverName(provisionalCalendar.getApproverName())
				.approverDesignation(provisionalCalendar.getApproverDesignation())
				.dateOfSittingInputs(provisionalCalendar.getDateOfSittingInputs())
				.listOfDocument(provisionalCalendar.getListOfDocument())
				.applicationId(provisionalCalendar.getApplicationId()).status(provisionalCalendar.getCurrentStatus())
				.applicationInstanceDTO(ApplicationInstanceDTO.builder().id(provisionalCalendar.getId())
						.applicationId(provisionalCalendar.getApplicationId())
						.applicationNo(provisionalCalendar.getApplicationNo())
						.currentStatus(provisionalCalendar.getCurrentStatus()).build())
				.build();
	}

}
