package com.dsp.agendaPapers.provisionalcalendarfinal.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsp.agendaPapers.dto.ApplicationInstanceDTO;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.InputInstanceDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarGenerateDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarInputDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarTableOfficeDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberRequest;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberResponse;
import com.dsp.agendaPapers.provisionalcalendarfinal.entity.InputInstance;
//import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarGenerateDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.entity.ProvisionalCalendar;
import com.dsp.agendaPapers.provisionalcalendarfinal.feignclient.BearerClient;
import com.dsp.agendaPapers.provisionalcalendarfinal.mapper.InputInstanceMapper;
import com.dsp.agendaPapers.provisionalcalendarfinal.mapper.ProvisionalCalendarDTOMapper;
import com.dsp.agendaPapers.provisionalcalendarfinal.repository.InputInstanceRepository;
import com.dsp.agendaPapers.provisionalcalendarfinal.repository.ProvisionalCalendarRepository;
import com.dsp.agendaPapers.repository.ApplicationInstanceRepository;

import jakarta.transaction.Transactional;

@Service
public class ProvisionalCalendarServiceImpl implements ProvisionalCalendarService {

	@Autowired
	private ProvisionalCalendarRepository provisionalCalendarRepository;
	@Autowired
	InputInstanceRepository inputInstanceRepository;
	@Autowired
	ApplicationInstanceRepository applicationInstanceRepository;
	@Autowired
	private BearerClient bearerClient;

	// Creating Provisional Calender
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
				.inputStatus(provisionalCalendarDTO.getInputStatus()).remarks(provisionalCalendarDTO.getRemarks())

				// application instance
				.id(provisionalCalendarDTO.getId()).applicationNo(provisionalCalendarDTO.getApplicationNo())
				.currentStatus(provisionalCalendarDTO.getCurrentStatus())
				.publishedDocumentUrl(provisionalCalendarDTO.getPublishedDocumentUrl())
				// .processInstanceId(provisionalCalendarDTO.getProcessInstanceId())
				.isPublished(provisionalCalendarDTO.getIsPublished())
				.lokSabhaSession(provisionalCalendarDTO.getLokSabhaSession())
				.lokSabhaSessionNumber(provisionalCalendarDTO.getLokSabhaSessionNumber())
				.rajyaSabhaSession(provisionalCalendarDTO.getRajyaSabhaSession())
				.documentDate(provisionalCalendarDTO.getDateOfInput().toLocalDate())
				// .otherInfo((Map<String, Object>) questionbranchInputDTO.getOtherInfo())
				.applicationId(provisionalCalendarDTO.getApplicationId()).draft(false).build();

		ProvisionalCalendar savedEntity = provisionalCalendarRepository.save(provisionalCalendar);
		savedEntity.setRefId(savedEntity.getId());
		savedEntity = provisionalCalendarRepository.save(savedEntity);
		return mapToDto(savedEntity);
	}

	// getById ProvisionalCalender

	@Override
	public ProvisionalCalendarDTO getProvisionalCalendarById(Long id) {
		ProvisionalCalendar provisionalCalendar = provisionalCalendarRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("ProvisionalCalendar not found with id " + id));
		ProvisionalCalendarDTO responseDTO = mapToDto(provisionalCalendar);
		try {
//					InputInstanceDTO responseDTO = inputInstanceRepository
//					.findByRefId(singleDTO.getId())
//					.map(InputInstanceMapper::toDTO)
//					.orElse(null);
			InputInstanceDTO dto = inputInstanceRepository.findByRefId(responseDTO.getId())
					.map(InputInstanceMapper::toDTO)
					.orElseThrow(() -> new Exception("InputInstance not found for refId: " + responseDTO.getId()));
			System.out.println("###########3 responseDTO of Input Instance DTO");
			System.out.println(dto);
			responseDTO.setInputInstanceDTO(dto);
		} catch (Exception e) {

			System.out.println("Input Instance Not found for PC");
			e.printStackTrace();
		}
		return responseDTO;
	}

	// getAll provisional calender
	@Override
	public List<ProvisionalCalendarDTO> getAllProvisionalCalendars() {
		List<ProvisionalCalendarDTO> dto = provisionalCalendarRepository.findAll().stream().map(this::mapToDto)
				.collect(Collectors.toList());
		for (ProvisionalCalendarDTO singleDTO : dto) {
			// InputInstanceDTO
			// responceDTO=inputInstanceRepository.findById(singleDTO.getId()).
			// map(InputInstanceMapper::toDTO);
			try {
				InputInstanceDTO responseDTO = inputInstanceRepository.findById(singleDTO.getId())
						.map(InputInstanceMapper::toDTO).orElse(null);
				singleDTO.setInputInstanceDTO(responseDTO);
			} catch (Exception e) {
				System.out.println("Input Instance Not found for PC");
				throw e;
			}
		}
		System.out.println("input instance DTO getAll");
		System.out.println(dto);
		return dto;
	}

	// update() Provisional Calender
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
			// adding the new field inputstaus and remarks
			existingEntity.setInputStatus(provisionalCalendarDTO.getInputStatus());
			existingEntity.setRemarks(provisionalCalendarDTO.getRemarks());
			existingEntity.setDocumentDate(provisionalCalendarDTO.getDateOfInput().toLocalDate());
			existingEntity.setRefId(provisionalCalendarDTO.getId());

			ProvisionalCalendar updatedEntity = provisionalCalendarRepository.save(existingEntity);
			return mapToDto(updatedEntity);
		}).orElseThrow(() -> new RuntimeException("ProvisionalCalendar not found with id " + id));
	}

	// delete() provisional calender
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

	// save provisional calender input()
	@Override
	public ProvisionalCalendarInputDTO createProvisionalCalendarInput(ProvisionalCalendarInputDTO inputDTO) {
		// Map DTO to Entity
		// changes for the input
		// generating Application No with FeinClient
		SerialNumberRequest serialNumberRequest = new SerialNumberRequest();
		serialNumberRequest.setAppId(inputDTO.getApplicationId());
		if (inputDTO.getLokSabhaSessionNo() == null) {
			serialNumberRequest.setSessionId(inputDTO.getRsSessionNo());

		} else {
			serialNumberRequest.setSessionId(inputDTO.getLokSabhaSessionNo());
		}
		SerialNumberResponse applicationNo = bearerClient.getResource(serialNumberRequest);

		ProvisionalCalendar provisionalCalendar = ProvisionalCalendar.builder().dateOfInput(inputDTO.getDateOfInput())
				.language(inputDTO.getLanguage()).submittedBy(inputDTO.getSubmittedBy())
				.submittedByBranch(inputDTO.getSubmittedByBranch()).subject(inputDTO.getSubject())
				.inputText(inputDTO.getInputText()).footnoteText(inputDTO.getFootnoteText())
				.approverName(inputDTO.getApproverName()).approverDesignation(inputDTO.getApproverDesignation())
				.dateOfSittingInputs(inputDTO.getDateOfSittingInputs()).listOfDocument(inputDTO.getListOfDocument())
				.applicationId(inputDTO.getApplicationId()).lokSabhaSession(inputDTO.getLokSabhaSession())
				.lokSabhaSessionNumber(inputDTO.getLokSabhaSessionNumber())
				.rajyaSabhaSession(inputDTO.getRajyaSabhaSession())
				.documentDate(inputDTO.getDateOfInput().toLocalDate())
				// .applicationNo(applicationNo)
				.applicationNo(applicationNo.getId())// Set applicationNo
				// input status and remarks
				.currentStatus(inputDTO.getInputStatus()).inputStatus(inputDTO.getInputStatus())
				.remarks(inputDTO.getRemarks()).refId(inputDTO.getId()).build();
		ProvisionalCalendar savedEntity = provisionalCalendarRepository.save(provisionalCalendar);
		savedEntity.setRefId(savedEntity.getId());
		savedEntity = provisionalCalendarRepository.save(savedEntity);
		System.out.println("calender input with input instance");
		InputInstance inputInstanceEntity = InputInstance.builder().id(savedEntity.getId())
				.applicationId(savedEntity.getApplicationId()).applicationNo(savedEntity.getApplicationNo())
				.refId(savedEntity.getRefId()).documentDate(savedEntity.getDateOfInput().toLocalDate())
				.currentStatus(savedEntity.getInputStatus()).build();
		System.out.println(inputInstanceEntity);

		inputInstanceEntity = inputInstanceRepository.save(inputInstanceEntity);
		System.out.println("Input Instances");
		System.out.println(inputInstanceEntity);
		ProvisionalCalendarInputDTO responceDTO = mapToDtoPCinput(savedEntity);
		responceDTO.setInputInstanceDTO(InputInstanceMapper.toDTO(inputInstanceEntity));
		System.out.println("response with Input instance");
		System.out.println(responceDTO);
		return responceDTO;

	}
	// save provisional calender tableOffice()

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
		// provisionalCalendar.setRefId(provisionalCalendarTableOfficeDTO.getId());
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

	// Mapper() in service
	private ProvisionalCalendarDTO mapToDto(ProvisionalCalendar provisionalCalendar) {

		SerialNumberRequest serialNumberRequest = new SerialNumberRequest();
		serialNumberRequest.setAppId(provisionalCalendar.getApplicationId());
		serialNumberRequest.setSessionId(2l);

		SerialNumberResponse applicationNo = bearerClient.getResource(serialNumberRequest);
		System.out.println(applicationNo.getId());
		provisionalCalendar.setApplicationNo(applicationNo.getId());
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
				.inputStatus(provisionalCalendar.getInputStatus()).remarks(provisionalCalendar.getRemarks())
				.applicationId(provisionalCalendar.getApplicationId())
				.applicationInstanceDTO(ApplicationInstanceDTO.builder().id(provisionalCalendar.getId())
						.applicationId(provisionalCalendar.getApplicationId())
						.applicationNo(provisionalCalendar.getApplicationNo())
						.currentStatus(provisionalCalendar.getCurrentStatus())
						.documentDate(provisionalCalendar.getDateOfInput().toLocalDate())
						.refId(provisionalCalendar.getRefId()).build())
				.build();
	}

	// @Override
	// public ProvisionalCalendarGenerateDTO
	// createProvisionalCalendarGenerate(ProvisionalCalendarGenerateDTO
	// provisionalCalendarGenerateDTO) {
	//
	// Optional<ProvisionalCalendar> optionalProvisionalCalendar =
	// provisionalCalendarRepository.findById(provisionalCalendarGenerateDTO.getProvisionalCalendarInputId());
	//
	// if (optionalProvisionalCalendar.isEmpty()) {
	// throw new ResourceNotFoundException("ProvisionalCalendar with ID " +
	// provisionalCalendarGenerateDTO.getProvisionalCalendarInputId() + " not
	// found");
	// }
	//
	// // Retrieve the existing ProvisionalCalendar entity
	// ProvisionalCalendar provisionalCalendar = optionalProvisionalCalendar.get();
	//
	// // Update the fields with values from the DTO
	// provisionalCalendar.setContent(provisionalCalendarGenerateDTO.getContent());
	//
	// // Save the updated entity
	// ProvisionalCalendar updatedProvisionalCalendar =
	// provisionalCalendarRepository.save(provisionalCalendar);
	//
	// // Map the updated entity back to a DTO
	// // Map the updated entity back to a ProvisionalCalendarTableOfficeDTO
	// return ProvisionalCalendarGenerateDTO.builder()
	// .id(updatedProvisionalCalendar.getId())
	// .content(updatedProvisionalCalendar.getContent())
	//
	// .provisionalCalendarInputId(provisionalCalendarGenerateDTO.getProvisionalCalendarInputId())
	// .build();
	// }
	// generated is working fine
	@Override
	public ProvisionalCalendarGenerateDTO createProvisionalCalendarGenerate(
			ProvisionalCalendarGenerateDTO provisionalCalendarGenerateDTO) {
		// get application number
		// Long applicationNo =

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
		provisionalCalendar.setStatus(provisionalCalendarGenerateDTO.getStatus());
		provisionalCalendar.setCurrentStatus(provisionalCalendarGenerateDTO.getStatus());

		// Update Application Instance
		applicationInstanceRepository.updateApplicationInstanceId(provisionalCalendarGenerateDTO.getApplicationId(),
				provisionalCalendarGenerateDTO.getProvisionalCalendarInputId());

		// Save the updated entity
		ProvisionalCalendar updatedProvisionalCalendar = provisionalCalendarRepository.save(provisionalCalendar);

		// Map the updated entity back to a ProvisionalCalendarGenerateDTO
		return ProvisionalCalendarGenerateDTO.builder().id(updatedProvisionalCalendar.getId())
				.content(updatedProvisionalCalendar.getContent()).status(updatedProvisionalCalendar.getStatus())
				.provisionalCalendarInputId(provisionalCalendarGenerateDTO.getProvisionalCalendarInputId())
				.applicationId(provisionalCalendar.getApplicationId()).build();
	}

	// getAll working fine

	// @Override
	// public List<ProvisionalCalendarDTO>
	// getAllProvisionalCalendarsBetweenDates(LocalDate startDate, LocalDate
	// endDate,String inputStatus) {
	// List<ProvisionalCalendarDTO> response=new ArrayList<>();

	//
	// if (startDate == null || endDate == null)
	// {
	// // If no dates are provided, fetch all records

	// if(inputStatus!=null)
	// {
	// response=
	// provisionalCalendarRepository.findByInputStatus(inputStatus).stream()
	// .map(ProvisionalCalendarDTOMapper::toDTO).toList();
	// }
	// else{
	// response=
	// provisionalCalendarRepository.findAll().stream().map(ProvisionalCalendarDTOMapper::toDTO).toList();
	////
	// }

	//// return response;
	// }
	// else {
	//
	// // Convert LocalDate to LocalDateTime for precise filtering
	// LocalDateTime startDateTime = startDate.atStartOfDay();
	// LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
	//
	// if (inputStatus != null) {
	// response=
	// provisionalCalendarRepository.findByInputStatus(inputStatus).stream()
	// .map(ProvisionalCalendarDTOMapper::toDTO).toList();
	// }
	// // Fetch records between the date range
	// response =
	// provisionalCalendarRepository.findByDateOfInputBetween(startDateTime,
	// endDateTime).stream()
	// .map(ProvisionalCalendarDTOMapper::toDTO).toList();

	// }
	//// return
	// provisionalCalendarRepository.findByDateOfInputBetween(startDateTime,
	// endDateTime).stream()
	//// .map(ProvisionalCalendarDTOMapper::toDTO).toList();
	// return response;
	//
	// }

	// update for the input status and remarks testing

	@Override
	public ProvisionalCalendarInputDTO updateInputStatus(Long id, String inputStatus, String remarks) {
		// Find the ProvisionalCalendar entity by ID
		Optional<ProvisionalCalendar> optionalEntity = provisionalCalendarRepository.findById(id);

		if (optionalEntity.isPresent()) {
			ProvisionalCalendar existingEntity = optionalEntity.get();

			// Update input status
			existingEntity.setInputStatus(inputStatus);

			// Ensure remarks is never null. Set it to an empty string if null.
			if (remarks == null) {
				remarks = ""; // Or set a default value, like "No remarks provided"
			}
			existingEntity.setRemarks(remarks);

			// Save the updated entity back to the repository
			ProvisionalCalendar updatedEntity = provisionalCalendarRepository.save(existingEntity);

			// Map the updated entity to DTO and return
			return mapToInputDto(updatedEntity);
		} else {
			throw new RuntimeException("ProvisionalCalendar not found with id " + id);
		}
	}

	private ProvisionalCalendarInputDTO mapToInputDto(ProvisionalCalendar entity) {
		return ProvisionalCalendarInputDTO.builder().id(entity.getId()).inputStatus(entity.getInputStatus())
				.remarks(entity.getRemarks())
				// Ensure the field name is consistent
				.build();
	}

	// // for input status will be update in return get All for testing purpose
	// working
	//
	// @Override
	// public List<ProvisionalCalendarDTO> getAllProvisionalCalendarsBetweenDates(
	// LocalDate startDate, LocalDate endDate, String inputStatus) {
	// List<ProvisionalCalendarDTO> response;
	//
	// if (startDate == null || endDate == null) {
	// // Handle cases with only inputStatus filter
	// if (inputStatus != null) {
	// response =
	// provisionalCalendarRepository.findByInputStatus(inputStatus).stream()
	// .map(ProvisionalCalendarDTOMapper::toDTO)
	// .toList();
	// } else {
	// // Fetch all records if no filters provided
	// response = provisionalCalendarRepository.findAll().stream()
	// .map(ProvisionalCalendarDTOMapper::toDTO)
	// .toList();
	// }
	// } else {
	// // Handle date range filtering
	// LocalDateTime startDateTime = startDate.atStartOfDay();
	// LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
	//
	// if (inputStatus != null) {
	// response = provisionalCalendarRepository
	// .findByInputStatusAndDateOfInputBetween(inputStatus, startDateTime,
	// endDateTime)
	// .stream()
	// .map(ProvisionalCalendarDTOMapper::toDTO)
	// .toList();
	// } else {
	// response =
	// provisionalCalendarRepository.findByDateOfInputBetween(startDateTime,
	// endDateTime).stream()
	// .map(ProvisionalCalendarDTOMapper::toDTO)
	// .toList();
	// }
	//
	// }
	//
	// return response;
	// }

	// getAll deployed apis is that deployed and Working fine
	// @Override
	// public List<ProvisionalCalendarDTO> getAllProvisionalCalendarsBetweenDates(
	// LocalDate startDate, LocalDate endDate, String inputStatus) {
	// List<ProvisionalCalendarDTO> response;
	// try {
	// if (startDate == null || endDate == null) {
	// // Handle cases with only inputStatus filter
	// if (inputStatus != null) {
	// response =
	// provisionalCalendarRepository.findByInputStatus(inputStatus).stream()
	// .map(ProvisionalCalendarDTOMapper::toDTO)
	// .collect(Collectors.toList());
	// } else {
	// // Fetch all records if no filters provided
	// response = provisionalCalendarRepository.findAll().stream()
	// .map(ProvisionalCalendarDTOMapper::toDTO)
	// .collect(Collectors.toList());
	// }
	// } else {
	// // Handle date range filtering
	// LocalDateTime startDateTime = startDate.atStartOfDay();
	// LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
	//
	// if (inputStatus != null) {
	// response = provisionalCalendarRepository
	// .findByInputStatusAndDateOfInputBetween(inputStatus, startDateTime,
	// endDateTime)
	// .stream()
	// .map(ProvisionalCalendarDTOMapper::toDTO)
	// .collect(Collectors.toList());
	// } else {
	// response = provisionalCalendarRepository
	// .findByDateOfInputBetween(startDateTime, endDateTime)
	// .stream()
	// .map(ProvisionalCalendarDTOMapper::toDTO)
	// .collect(Collectors.toList());
	// }
	// }
	// return response;
	// } catch (Exception ex) {
	// // Log the exception
	// entries: " + ex.getMessage());
	// ex.printStackTrace(); // Print stack trace for debugging
	//
	// // Optionally, rethrow the exception or return an empty list
	// throw new RuntimeException("An error occurred while fetching Provisional
	// Calendar entries", ex);
	// }
	// }

	// get All filter with testing for submiited by
	@Override
	public List<ProvisionalCalendarDTO> getAllProvisionalCalendarsBetweenDates(LocalDate startDate, LocalDate endDate,
			String inputStatus, String submittedBy) {
		List<ProvisionalCalendarDTO> response;
		try {
			System.out.println("@@@@@@@@@@@");
			if (startDate == null || endDate == null) {
				// Handle cases with only inputStatus and submittedBy filters
				if (inputStatus != null && submittedBy != null) {
					response = provisionalCalendarRepository.findByInputStatusAndSubmittedBy(inputStatus, submittedBy)
							.stream().map(ProvisionalCalendarDTOMapper::toDTO).collect(Collectors.toList());
					System.out.println(response);

				} else if (inputStatus != null) {
					response = provisionalCalendarRepository.findByInputStatus(inputStatus).stream()
							.map(ProvisionalCalendarDTOMapper::toDTO).collect(Collectors.toList());
					System.out.println(response);

				} else if (submittedBy != null) {
					response = provisionalCalendarRepository.findBySubmittedBy(submittedBy).stream()
							.map(ProvisionalCalendarDTOMapper::toDTO).collect(Collectors.toList());

				} else {
					// Fetch all records if no filters provided
					response = provisionalCalendarRepository.findAll().stream().map(ProvisionalCalendarDTOMapper::toDTO)
							.collect(Collectors.toList());
				}
				for (ProvisionalCalendarDTO singleDTO : response) {
					System.out.println(singleDTO);
					// InputInstanceDTO
					// responceDTO=inputInstanceRepository.findById(singleDTO.getId()).
					// map(InputInstanceMapper::toDTO);
					try {
//					InputInstanceDTO responseDTO = inputInstanceRepository
//					.findByRefId(singleDTO.getId())
//					.map(InputInstanceMapper::toDTO)
//
//
//
//
//					.orElse(null);
						InputInstanceDTO responseDTO = inputInstanceRepository.findByRefId(singleDTO.getId())
								.map(InputInstanceMapper::toDTO).orElseThrow(
										() -> new Exception("InputInstance not found for refId: " + singleDTO.getId()));
						System.out.println("###########3 responseDTO of Input Instance DTO");
						System.out.println(responseDTO);
						singleDTO.setInputInstanceDTO(responseDTO);
					} catch (Exception e) {

						System.out.println("Input Instance Not found for PC");
						e.printStackTrace();
					}
				}
			} else {

				// Handle date range filtering
//				LocalDateTime startDateTime = startDate.atStartOfDay();
				LocalDateTime startDateTime = startDate.atStartOfDay();
				LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
				if (inputStatus != null && submittedBy != null) {
					response = provisionalCalendarRepository
							.findByInputStatusAndSubmittedByAndDateOfInputBetween(inputStatus, submittedBy,
									startDateTime, endDateTime)
							.stream().map(ProvisionalCalendarDTOMapper::toDTO).collect(Collectors.toList());

				} else if (inputStatus != null) {
					response = provisionalCalendarRepository
							.findByInputStatusAndDateOfInputBetween(inputStatus, startDateTime, endDateTime).stream()
							.map(ProvisionalCalendarDTOMapper::toDTO).collect(Collectors.toList());

				} else if (submittedBy != null) {
					response = provisionalCalendarRepository
							.findBySubmittedByAndDateOfInputBetween(submittedBy, startDateTime, endDateTime).stream()
							.map(ProvisionalCalendarDTOMapper::toDTO).collect(Collectors.toList());

				} else {
					response = provisionalCalendarRepository.findByDateOfInputBetween(startDateTime, endDateTime)
							.stream().map(ProvisionalCalendarDTOMapper::toDTO).collect(Collectors.toList());

				}
				for (ProvisionalCalendarDTO singleDTO : response) {
					System.out.println(singleDTO);
					// InputInstanceDTO
					// responceDTO=inputInstanceRepository.findById(singleDTO.getId()).
					// map(InputInstanceMapper::toDTO);
					try {
//					InputInstanceDTO responseDTO = inputInstanceRepository
//					.findByRefId(singleDTO.getId())
//					.map(InputInstanceMapper::toDTO)
//					.orElse(null);
						InputInstanceDTO responseDTO = inputInstanceRepository.findByRefId(singleDTO.getId())
								.map(InputInstanceMapper::toDTO).orElseThrow(
										() -> new Exception("InputInstance not found for refId: " + singleDTO.getId()));
						System.out.println("###########3 responseDTO of Input Instance DTO");
						System.out.println(responseDTO);
						singleDTO.setInputInstanceDTO(responseDTO);
					} catch (Exception e) {

						System.out.println("Input Instance Not found for PC");
						e.printStackTrace();
					}
				}
			}

			return response;
		} catch (Exception ex) {
			// Log the exception
			System.err.println("Error occurred while fetching Provisional Calendar entries: " + ex.getMessage());
			ex.printStackTrace(); // Print stack trace for debugging

			// Optionally, rethrow the exception or return an empty list
			throw new RuntimeException("An error occurred while fetching Provisional Calendar entries", ex);
		}
	}

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
				.applicationId(provisionalCalendar.getApplicationId())
//				.status(provisionalCalendar.getCurrentStatus())
				// adding the remarks and input status
				.inputStatus(provisionalCalendar.getCurrentStatus()).remarks(provisionalCalendar.getRemarks())
				.applicationInstanceDTO(ApplicationInstanceDTO.builder().id(provisionalCalendar.getId())
						.applicationId(provisionalCalendar.getApplicationId())
						.applicationNo(provisionalCalendar.getApplicationNo())
						.currentStatus(provisionalCalendar.getCurrentStatus())
						.documentDate(provisionalCalendar.getDateOfInput().toLocalDate())
						.refId(provisionalCalendar.getRefId()).build())

				.build();
	}

}
