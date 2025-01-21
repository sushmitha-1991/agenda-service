package com.dsp.agendaPapers.provisionalcalendarfinal.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dsp.agendaPapers.dto.ApplicationInstanceDTO;
import com.dsp.agendaPapers.dto.UserDetailsDTO;
import com.dsp.agendaPapers.entity.BaseUser;
import com.dsp.agendaPapers.entity.MastersLokSabhaSession;
import com.dsp.agendaPapers.entity.MastersRajyaSabha;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.InputInstanceDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarGenerateDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarInputDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarTableOfficeDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberRequest;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberResponse;
import com.dsp.agendaPapers.provisionalcalendarfinal.entity.InputInstance;
import com.dsp.agendaPapers.provisionalcalendarfinal.entity.ProvisionalCalendar;
import com.dsp.agendaPapers.provisionalcalendarfinal.feignclient.BearerClient;
import com.dsp.agendaPapers.provisionalcalendarfinal.mapper.InputInstanceMapper;
import com.dsp.agendaPapers.provisionalcalendarfinal.mapper.ProvisionalCalendarDTOMapper;
import com.dsp.agendaPapers.provisionalcalendarfinal.repository.InputInstanceRepository;
import com.dsp.agendaPapers.provisionalcalendarfinal.repository.ProvisionalCalendarRepository;
import com.dsp.agendaPapers.repository.ApplicationInstanceRepository;
import com.dsp.agendaPapers.repository.MasterLSSRepository;
import com.dsp.agendaPapers.repository.MastersRSSRepository;
import com.dsp.agendaPapers.repository.MyUserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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

	// new Immpl code for the get All entity manager

	@Autowired
	private MastersRSSRepository mastersRSSRepository;

	@Autowired
	private MasterLSSRepository masterLSSRepository;
	@Autowired
	private MyUserRepository userRepository;
	@PersistenceContext
	private EntityManager entityManager;

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
				.generatedFlag(provisionalCalendarDTO.getGeneratedFlag())
				.content(provisionalCalendarDTO.getContent())
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
				.documentDate(provisionalCalendarDTO.getDateOfInput())
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
			InputInstance entity = inputInstanceRepository.findByRefId(responseDTO.getId())
					.orElseThrow(() -> new Exception("InputInstance not found for refId: " + responseDTO.getId()));
			if (entity.getApplicationInstanceId() != null) {
				if (!provisionalCalendar.getCurrentStatus().equalsIgnoreCase("returned")) {
					if (provisionalCalendar.getCurrentStatus().equalsIgnoreCase("published")) {
						entity.setPendingAt(null);
					}
					responseDTO.setInputStatus(provisionalCalendar.getCurrentStatus());
					entity.setCurrentStatus(provisionalCalendar.getCurrentStatus());
				}
			}
			InputInstanceDTO dto = InputInstanceMapper.toDTO(entity);
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

		return dto;
	}

	// update() Provisional Calender
	@Override
	public ProvisionalCalendarDTO updateProvisionalCalendar(Long id, ProvisionalCalendarDTO provisionalCalendarDTO) {

		ProvisionalCalendar existingEntity = provisionalCalendarRepository.findById(id).get();
		ProvisionalCalendar entityNew = ProvisionalCalendarDTOMapper.toEntity(provisionalCalendarDTO);

		Field[] fields = entityNew.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true); // Allow access to private fields
			try {
				Object newValue = field.get(entityNew);
				if (newValue != null) {
					Field entityField = existingEntity.getClass().getDeclaredField(field.getName());
					entityField.setAccessible(true);
					entityField.set(existingEntity, newValue);
				}
			} catch (NoSuchFieldException | IllegalAccessException e) {
				throw new RuntimeException("Error updating field: " + field.getName(), e);
			}
		}

		existingEntity.setCurrentStatus(existingEntity.getInputStatus());
		existingEntity.setDocumentDate(existingEntity.getDateOfInput());

		// return provisionalCalendarRepository.findById(id).map(existingEntity -> {
		// existingEntity.setDateOfInput(provisionalCalendarDTO.getDateOfInput());
		// existingEntity.setLanguage(provisionalCalendarDTO.getLanguage());
		// existingEntity.setSubmittedBy(provisionalCalendarDTO.getSubmittedBy());
		// existingEntity.setSubmittedByBranch(provisionalCalendarDTO.getSubmittedByBranch());
		// existingEntity.setSubject(provisionalCalendarDTO.getSubject());
		// existingEntity.setInputText(provisionalCalendarDTO.getInputText());
		// existingEntity.setFootnoteText(provisionalCalendarDTO.getFootnoteText());
		// existingEntity.setApproverName(provisionalCalendarDTO.getApproverName());
		// existingEntity.setApproverDesignation(provisionalCalendarDTO.getApproverDesignation());
		// existingEntity.setDateOfSittingInputs(provisionalCalendarDTO.getDateOfSittingInputs());
		// existingEntity.setListOfDocument(provisionalCalendarDTO.getListOfDocument());
		// existingEntity.setGroupchartsLS(provisionalCalendarDTO.getGroupchartsLS());
		// existingEntity.setGroupchartsRS(provisionalCalendarDTO.getGroupchartsRS());
		// existingEntity.setListOfDocumentTableOffice(provisionalCalendarDTO.getListOfDocumentTableOffice());
		// existingEntity.setListOfDocumentTableOfficeFootnote(
		// provisionalCalendarDTO.getListOfDocumentTableOfficeFootnote());
		// existingEntity.setListOfDocumentTableOfficeNote(provisionalCalendarDTO.getListOfDocumentTableOfficeNote());
		// existingEntity
		// .setDateOfSittingInputsTableOffice(provisionalCalendarDTO.getDateOfSittingInputsTableOffice());
		// existingEntity.setDateOfInputTableOffice(provisionalCalendarDTO.getDateOfInputTableOffice());
		// existingEntity.setInputTextTableOffice(provisionalCalendarDTO.getInputTextTableOffice());
		// existingEntity.setRajyaSabhaSession(provisionalCalendarDTO.getRajyaSabhaSessionNo());
		// existingEntity.setGeneratedFlag(provisionalCalendarDTO.getGeneratedFlag());
		// existingEntity.setContent(provisionalCalendarDTO.getContent());
		// // adding the new field inputstaus and remarks
		// existingEntity.setInputStatus(provisionalCalendarDTO.getInputStatus());
		// existingEntity.setRemarks(provisionalCalendarDTO.getRemarks());
		// existingEntity.setDocumentDate(provisionalCalendarDTO.getDateOfInput().toLocalDate());
		// existingEntity.setStatus(provisionalCalendarDTO.getStatus());
		// existingEntity.setCurrentStatus(provisionalCalendarDTO.getCurrentStatus());
		// existingEntity.setRefId(provisionalCalendarDTO.getId());

		ProvisionalCalendar updatedEntity = provisionalCalendarRepository.save(existingEntity);
		// changes for status update by sushmitha
		InputInstance instanceUpdate = inputInstanceRepository.findByRefId(existingEntity.getId()).get();
		instanceUpdate.setCurrentStatus(provisionalCalendarDTO.getInputStatus());
		Long houseId = null;
		// String houseAbbr = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsDTO) {
			houseId = ((UserDetailsDTO) principal).getHouseId();
			// houseAbbr = ((UserDetailsDTO) principal).getHouseAbbr();
		}
		if (houseId == 1) {
			instanceUpdate.setPendingAt("Legislative Section");
		}
		if (houseId == 2) {
			instanceUpdate.setPendingAt("Table Office A");

		}
		instanceUpdate = inputInstanceRepository.save(instanceUpdate);
		// ends here
		return mapToDto(updatedEntity);
		// }).orElseThrow(() -> new RuntimeException("ProvisionalCalendar not found with
		// id " + id));
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
		/*
		 * eyJhbGciOiJIUzI1NiJ9.
		 * eyJzdWIiOiJwbHVfcnNfc2EiLCJob3VzZUlkIjoxLCJob3VzZUFiYnIiOiJSUyIsImlhdCI6MTczNjg1Mzg3NiwiZXhwIjoxNzM2OTQwMjc2fQ
		 * .CC18fAwLo5r4wJ6gStn2v5VdxulRq7rvno1561-clK8
		 */
		Long houseId = null;
		// String houseAbbr = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsDTO) {
			houseId = ((UserDetailsDTO) principal).getHouseId();
			// houseAbbr = ((UserDetailsDTO) principal).getHouseAbbr();
		}
		SerialNumberRequest serialNumberRequest = new SerialNumberRequest();
		serialNumberRequest.setAppId(inputDTO.getApplicationId());
		if (houseId == 1) {
			serialNumberRequest.setSessionId(inputDTO.getRsSessionNo());
		}
		if (houseId == 2) {
			serialNumberRequest.setSessionId(inputDTO.getLokSabhaSessionNo());
		}
		// System.out.println("house abbr");
		// System.out.println(houseAbbr);
		// if (houseAbbr=="RS") {
		// serialNumberRequest.setSessionId(inputDTO.getRsSessionNo());
		// }
		// if (houseAbbr == "LS") {
		// serialNumberRequest.setSessionId(inputDTO.getLokSabhaSessionNo());
		// }

		SerialNumberResponse applicationNo = bearerClient.getResource(serialNumberRequest);
		ProvisionalCalendar provisionalCalendar = ProvisionalCalendar.builder()
				.dateOfInput(inputDTO.getDateOfInput())
				.language(inputDTO.getLanguage()).submittedBy(inputDTO.getSubmittedBy())
				.submittedByBranch(inputDTO.getSubmittedByBranch()).subject(inputDTO.getSubject())
				.inputText(inputDTO.getInputText()).footnoteText(inputDTO.getFootnoteText())
				.approverName(inputDTO.getApproverName()).approverDesignation(inputDTO.getApproverDesignation())
				.dateOfSittingInputs(inputDTO.getDateOfSittingInputs())
				// sushmitha added
				.rsSessionNo(inputDTO.getRsSessionNo())
				.lokSabhaSessionNo(inputDTO.getLokSabhaSessionNo())
				.listOfDocument(inputDTO.getListOfDocument())
				.applicationId(inputDTO.getApplicationId()).lokSabhaSession(inputDTO.getLokSabhaSession())
				.lokSabhaSessionNumber(inputDTO.getLokSabhaSessionNumber())
				.rajyaSabhaSession(inputDTO.getRajyaSabhaSession())
				.documentDate(inputDTO.getDateOfInput())
				// .applicationNo(applicationNo)
				.applicationNo(applicationNo.getId())
				// Set applicationNo
				.inputRefNo(applicationNo.getId())
				// input status and remarks
				.currentStatus(inputDTO.getInputStatus()).inputStatus(inputDTO.getInputStatus())
				.remarks(inputDTO.getRemarks()).refId(inputDTO.getId()).build();

		// changes for LS and RS
		if (houseId == 1) {
			MastersRajyaSabha session = mastersRSSRepository.findById(inputDTO.getRsSessionNo())
					.get();
			provisionalCalendar.setRsSessionNo(session.getId());
			provisionalCalendar.setRajyaSabhaSession(session.getSessionNumber());
			provisionalCalendar.setLokSabhaSessionNo(null);
			provisionalCalendar.setLokSabhaSession("");
			provisionalCalendar.setLokSabhaSessionNumber("");

		} else if (houseId == 2) {
			MastersLokSabhaSession session = masterLSSRepository
					.findById(inputDTO.getLokSabhaSessionNo())
					.get();
			provisionalCalendar.setLokSabhaSessionNo(session.getId());
			provisionalCalendar.setLokSabhaSessionNumber(session.getSessionNumber());
			provisionalCalendar.setLokSabhaSession(session.getLokSabha().getLokSabhaNumber());
			provisionalCalendar.setRajyaSabhaSession("");
		}
		ProvisionalCalendar savedEntity = provisionalCalendarRepository.save(
				provisionalCalendar);
		savedEntity.setRefId(savedEntity.getId());
		savedEntity = provisionalCalendarRepository.save(savedEntity);
		System.out.println("calender input with input instance");
		// Changes by Sushmitha Starts here
		ProvisionalCalendarInputDTO responceDTO = mapToDtoPCinput(savedEntity);
		InputInstance inputInstanceEntity = InputInstance.builder().id(savedEntity.getId())
				.applicationId(savedEntity.getApplicationId()).applicationNo(savedEntity.getApplicationNo())
				.refId(savedEntity.getRefId()).documentDate(savedEntity.getDateOfInput())
				.currentStatus(savedEntity.getInputStatus())
				.lokSabhaSession(savedEntity.getLokSabhaSession())
				.lokSabhaSessionNumber(savedEntity.getLokSabhaSessionNumber())
				.rajyaSabhaSession(savedEntity.getRajyaSabhaSession())
				.build();
		if (houseId == 1) {
			inputInstanceEntity.setPendingAt("Legislative Section");
		} else if (houseId == 2) {
			inputInstanceEntity.setPendingAt("Table Office A");
		}
		inputInstanceEntity = inputInstanceRepository.save(inputInstanceEntity);
		responceDTO.setInputInstanceDTO(InputInstanceMapper.toDTO(inputInstanceEntity));
		// Ends Here
		return responceDTO;

	}
	// save provisional calender tableOffice()

	@Override
	public ProvisionalCalendarTableOfficeDTO createProvisionalCalendarTableOffice(
			ProvisionalCalendarTableOfficeDTO provisionalCalendarTableOfficeDTO) {

		// changes on 07-01-2025
		Long houseId = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsDTO) {
			houseId = ((UserDetailsDTO) principal).getHouseId();
		}

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
		// adding new line for date update

		// provisionalCalendar.setDateOfInput(provisionalCalendarTableOfficeDTO.getDateOfInputTableOffice());
		provisionalCalendar.setDocumentDate(provisionalCalendarTableOfficeDTO.getDateOfInputTableOffice());

		// changes on 07-01-2025
		provisionalCalendar.setApplicationId(provisionalCalendarTableOfficeDTO.getApplicationId());
		// 07-01-2025 ends here
		provisionalCalendar.setInputStatus("In Progress");
		// Save the updated entity
		ProvisionalCalendar updatedProvisionalCalendar = provisionalCalendarRepository.save(provisionalCalendar);
		// Input instance status and date update

		InputInstance inputInstance = inputInstanceRepository.findByRefId(updatedProvisionalCalendar.getId()).get();
		inputInstance.setCurrentStatus(updatedProvisionalCalendar.getInputStatus());
		if (houseId == 1) {
			inputInstance.setPendingAt("Legislative Section");
		}
		if (houseId == 2) {
			inputInstance.setPendingAt("Table Office A");
		}

		inputInstance = inputInstanceRepository.save(inputInstance);

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

	// Mapper() in service
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
				.inputStatus(provisionalCalendar.getInputStatus()).remarks(provisionalCalendar.getRemarks())
				.applicationId(provisionalCalendar.getApplicationId())
				.applicationInstanceDTO(ApplicationInstanceDTO.builder().id(provisionalCalendar.getId())
						.applicationId(provisionalCalendar.getApplicationId())
						.applicationNo(provisionalCalendar.getApplicationNo())
						.currentStatus(provisionalCalendar.getCurrentStatus())
						.documentDate(provisionalCalendar.getDocumentDate())
						.lokSabhaSession(provisionalCalendar.getLokSabhaSession())
						.lokSabhaSessionNumber(provisionalCalendar.getLokSabhaSessionNumber())
						.rajyaSabhaSession(provisionalCalendar.getRajyaSabhaSession())
						.refId(provisionalCalendar.getRefId()).build())
				.build();
	}

	// generated is working fine
	@Override
	public ProvisionalCalendarGenerateDTO createProvisionalCalendarGenerate(
			ProvisionalCalendarGenerateDTO provisionalCalendarGenerateDTO) {
		// get application number
		// Long applicationNo =
		// changes on 07-01-2025
		Long houseId = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsDTO) {
			houseId = ((UserDetailsDTO) principal).getHouseId();
		}

		SerialNumberRequest serialNumberRequest = new SerialNumberRequest();
		serialNumberRequest.setAppId(provisionalCalendarGenerateDTO.getApplicationId());

		if (houseId == 1) {
			serialNumberRequest.setSessionId(provisionalCalendarGenerateDTO.getRssNo());

		} else if (houseId == 2) {
			serialNumberRequest.setSessionId(provisionalCalendarGenerateDTO.getLssNo());
		}
		SerialNumberResponse applicationNo = bearerClient.getResource(serialNumberRequest);
		// changes ends here
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
		provisionalCalendar.setApplicationId(provisionalCalendarGenerateDTO.getApplicationId());

		// changes on 07-01-2025
		provisionalCalendar.setApplicationNo(applicationNo.getId());
		// ends here
		// changes for generated flag
		provisionalCalendar.setGeneratedFlag(true);

		// Save the updated entity
		ProvisionalCalendar updatedProvisionalCalendar = provisionalCalendarRepository.save(provisionalCalendar);

		InputInstance inputInstance = inputInstanceRepository
				.findByRefId(updatedProvisionalCalendar.getId()).get();

		inputInstance.setApplicationInstanceId(updatedProvisionalCalendar.getId());
		inputInstance = inputInstanceRepository.save(inputInstance);

		// Map the updated entity back to a ProvisionalCalendarGenerateDTO
		return ProvisionalCalendarGenerateDTO.builder().id(updatedProvisionalCalendar.getId())
				.content(updatedProvisionalCalendar.getContent()).status(updatedProvisionalCalendar.getStatus())
				.provisionalCalendarInputId(provisionalCalendarGenerateDTO.getProvisionalCalendarInputId())
				.applicationId(provisionalCalendarGenerateDTO.getApplicationId()).build();
	}

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
			// changes done by sushmitha
			InputInstance existingInstance = inputInstanceRepository.findByRefId(updatedEntity.getId()).get();

			existingInstance.setCurrentStatus(existingEntity.getInputStatus());
			if (inputStatus.equalsIgnoreCase("returned")) {
				BaseUser user = userRepository.findById(existingInstance.getCreatedBy()).get();
				existingInstance.setPendingAt(user.getFirstName() + " " + user.getLastName());
			}
			existingInstance = inputInstanceRepository.save(existingInstance);
			// changes ends here
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

	// get All new codes according to kaushik sir
	@Override
	public List<ProvisionalCalendarDTO> getAllProvisionalCalendarsBetweenDates(
			LocalDate startDate,
			LocalDate endDate,
			String inputStatus,
			String submittedBy) {
		List<ProvisionalCalendarDTO> response = new ArrayList<>();
		try {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<ProvisionalCalendar> cq = cb.createQuery(ProvisionalCalendar.class);
			Root<ProvisionalCalendar> root = cq.from(ProvisionalCalendar.class);

			// Initialize a conjunction predicate (always true initially)
			Predicate predicate = cb.conjunction();

			// Add filters dynamically
			if (startDate != null && endDate != null) {
				// LocalDate startDateTime = startDate.atStartOfDay();
				// LocalDate endDateTime = endDate
				// .atTime(23, 59, 59);
				predicate = cb.and(predicate, cb.between(root.get("dateOfInput"), startDate, endDate));
			}

			if (inputStatus != null && !inputStatus.trim().isEmpty()) {
				predicate = cb.and(predicate,
						cb.equal(cb.lower(root.get("inputStatus")), inputStatus.toLowerCase()));
			}
			// if (submittedBy != null && !submittedBy.trim().isEmpty()) {
			// String trimmedSubmittedBy = submittedBy.trim().toLowerCase();
			// predicate = cb.and(predicate,
			// cb.like(cb.lower(cb.trim(root.get("submittedBy"))), "%" + trimmedSubmittedBy
			// + "%"));
			// }

			if (submittedBy != null && !submittedBy.trim().isEmpty()) {
				String trimmedSubmittedBy = submittedBy.replaceAll("\\s", "").toLowerCase();
				predicate = cb.and(predicate,
						cb.like(
								cb.lower(cb.function("REPLACE", String.class, root.get("submittedBy"), cb.literal(" "),
										cb.literal(""))),
								"%" + trimmedSubmittedBy + "%"));
			}
			cq.where(predicate);
			TypedQuery<ProvisionalCalendar> query = entityManager.createQuery(cq);

			List<ProvisionalCalendar> provisionalCalendars = query.getResultList();
			/* For Differenciating lok Sabha And Rajya Sabha */
			// changes by Sushmitha
			Long houseId = null;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetailsDTO) {
				houseId = ((UserDetailsDTO) principal).getHouseId();
			}
			for (ProvisionalCalendar provisionalCalendar : provisionalCalendars) {

				if (houseId == 1) {
					if (provisionalCalendar.getRsSessionNo() != null) {
						// Convert entities to DTOs
						ProvisionalCalendarDTO dto = ProvisionalCalendarDTOMapper.toDTO(provisionalCalendar);
						Optional<InputInstance> instance = inputInstanceRepository.findByRefId(dto.getId());

						if (!instance.isEmpty()) {
							InputInstance inputInstance = instance.get();
							if (inputInstance.getApplicationInstanceId() != null) {
								if (!provisionalCalendar.getCurrentStatus().equalsIgnoreCase("returned")) {
									if (provisionalCalendar.getCurrentStatus().equalsIgnoreCase("published")) {
										inputInstance.setPendingAt(null);
									}
									dto.setInputStatus(provisionalCalendar.getCurrentStatus());
									inputInstance.setCurrentStatus(provisionalCalendar.getCurrentStatus());
									inputInstance = inputInstanceRepository.save(inputInstance);
									inputInstance = inputInstanceRepository.findById(inputInstance.getId()).get();
								}
							}
							dto.setInputInstanceDTO(InputInstanceMapper.toDTO(inputInstance));
						}
						response.add(dto);
					}
				} else if (houseId == 2) {
					if (provisionalCalendar.getLokSabhaSessionNo() != null) {
						ProvisionalCalendarDTO dto = ProvisionalCalendarDTOMapper.toDTO(provisionalCalendar);
						// Optional<InputInstance> instance =
						// inputInstanceRepository.findByRefId(dto.getId());
						// if (!instance.isEmpty()) {
						// dto.setInputInstanceDTO(InputInstanceMapper.toDTO(instance.get()));
						// }
						Optional<InputInstance> instance = inputInstanceRepository.findByRefId(dto.getId());

						if (!instance.isEmpty()) {
							InputInstance inputInstance = instance.get();
							if (inputInstance.getApplicationInstanceId() != null) {
								dto.setInputStatus(provisionalCalendar.getCurrentStatus());
								inputInstance.setCurrentStatus(provisionalCalendar.getCurrentStatus());
							}
							dto.setInputInstanceDTO(InputInstanceMapper.toDTO(inputInstance));
						}
						response.add(dto);
					}
				}
			}
			// for (ProvisionalCalendarDTO singleDTO : response) {
			// try {
			// InputInstanceDTO responseDTO =
			// inputInstanceRepository.findByRefId(singleDTO.getId())
			// .map(InputInstanceMapper::toDTO)
			// .orElseThrow(
			// () -> new Exception("InputInstance not found for refId: " +
			// singleDTO.getId()));
			// singleDTO.setInputInstanceDTO(responseDTO);
			// } catch (Exception e) {
			// System.err
			// .println("Input Instance not found for Provisional Calendar with ID: " +
			// singleDTO.getId());
			// e.printStackTrace();
			// }
			// }
		} catch (Exception ex) {
			// Log and rethrow the exception for debugging
			System.err.println("Error occurred while fetching Provisional Calendar entries: " + ex.getMessage());
			ex.printStackTrace();
			throw new RuntimeException("An error occurred while fetching Provisional Calendar entries", ex);
		}

		return response;
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
				.inputRefNo(provisionalCalendar.getInputRefNo())
				// .status(provisionalCalendar.getCurrentStatus())
				.lokSabhaSessionNo(provisionalCalendar.getLokSabhaSessionNo())
				.rsSessionNo(provisionalCalendar.getRsSessionNo())
				// adding the remarks and input status
				.inputStatus(provisionalCalendar.getCurrentStatus()).remarks(provisionalCalendar.getRemarks())
				.applicationInstanceDTO(ApplicationInstanceDTO.builder().id(provisionalCalendar.getId())
						.applicationId(provisionalCalendar.getApplicationId())
						.applicationNo(provisionalCalendar.getApplicationNo())
						.currentStatus(provisionalCalendar.getCurrentStatus())
						.documentDate(provisionalCalendar.getDateOfInput())
						.lokSabhaSession(provisionalCalendar.getLokSabhaSession())
						.lokSabhaSessionNumber(provisionalCalendar.getLokSabhaSessionNumber())
						.rajyaSabhaSession(provisionalCalendar.getRajyaSabhaSession())
						.refId(provisionalCalendar.getRefId()).build())

				.build();
	}

}
