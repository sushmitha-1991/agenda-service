package com.dsp.agendaPapers.summonNotification.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.dsp.agendaPapers.applicationPapers.dto.ApplicationPapersDTO;
import com.dsp.agendaPapers.applicationPapers.dto.TemplatesDTO;
import com.dsp.agendaPapers.applicationPapers.entity.ApplicationPapers;
import com.dsp.agendaPapers.applicationPapers.repository.ApplicationPapersRepository;
import com.dsp.agendaPapers.applicationPapers.service.ApplicationPapersService;
import com.dsp.agendaPapers.dto.UserDetailsDTO;
import com.dsp.agendaPapers.entity.MastersLokSabhaSession;
import com.dsp.agendaPapers.entity.MastersRajyaSabha;
import com.dsp.agendaPapers.exception.ExternalServiceException;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import com.dsp.agendaPapers.mapper.SummonNotificationMapper;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberRequest;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberResponse;
import com.dsp.agendaPapers.provisionalcalendarfinal.feignclient.BearerClient;
import com.dsp.agendaPapers.repository.MasterLSSRepository;
import com.dsp.agendaPapers.repository.MastersRSSRepository;
import com.dsp.agendaPapers.response.GenericResponse;
import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;
import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;
import com.dsp.agendaPapers.summonNotification.entity.SummonNotification;
import com.dsp.agendaPapers.summonNotification.repository.SummonNotificationRepository;
import com.dsp.agendaPapers.summonNotification.service.SummonNotificationService;

@Service
public class SummonNotificationServiceImpl implements SummonNotificationService {

	private static final Logger log = LoggerFactory.getLogger(SummonNotificationServiceImpl.class);

	@Autowired
	private SummonNotificationRepository summonNotificationRepository;

	@Autowired
	private ApplicationPapersService applicationPapersService;

	@Autowired
	private BearerClient bearerClient;
	@Autowired
	private MastersRSSRepository mastersRSSRepository;

	@Autowired
	private MasterLSSRepository masterLSSRepository;
	@Autowired
	private SummonNotificationMapper summonNotificationMapper;

	@Value("${application.instance.service.url}")
	private String applicationInstanceServiceUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApplicationPapersRepository applicationPapersRepository;

	@Value("${template.service.url}") // URL to validate template ID
	private String templateServiceUrl;

	@Transactional
	@Override
	public SummonNotificationDTO saveSummonNotification(SummonNotificationDTO summonNotificationDTO) {
		log.debug("Entering save Summon Notification with SummonNotificationDTO: {}", summonNotificationDTO);

		Long houseId = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsDTO) {
			houseId = ((UserDetailsDTO) principal).getHouseId();
		}
		SerialNumberRequest serialNumberRequest = new SerialNumberRequest();
		serialNumberRequest.setAppId(summonNotificationDTO.getApplicationId());
		if (houseId == 1) {
			serialNumberRequest.setSessionId(Long.parseLong(summonNotificationDTO.getRajyasabhaSessionNo()));
			summonNotificationDTO.setLokSabhaNo("");
			summonNotificationDTO.setSessionNo("");
		}
		if (houseId == 2) {
			serialNumberRequest.setSessionId(Long.parseLong(summonNotificationDTO.getSessionNo()));
			summonNotificationDTO.setRajyasabhaSessionNo("");
		}
		if (StringUtils.isBlank(summonNotificationDTO.getApplicationNo())) {
			// Fetch a new application number
			SerialNumberResponse applicationNo = bearerClient.getResource(serialNumberRequest);
			if (applicationNo != null && applicationNo.getId() != null) {
				summonNotificationDTO.setApplicationNo(applicationNo.getId());
			} else {
				throw new ExternalServiceException("Failed to generate application number");
			}
		}
		// else {
		// // If the application number is provided, ensure it is used
		// // SerialNumberResponse applicationNo =
		// bearerClient.getResource(serialNumberRequest);
		// summonNotificationDTO.setApplicationNo(applicationNo.getId());
		// }

		try {
			// Convert DTO to entity
			SummonNotification summonNotification = summonNotificationMapper.toEntity(summonNotificationDTO);
			log.debug("Converted SummonNotificationDTO to entity: {}", summonNotification);
			// Save SummonNotification entity first to generate the ID
			SummonNotification savedNotification = summonNotificationRepository.save(summonNotification);
			log.debug("First save - Generated SummonNotification ID: {}", savedNotification.getId());
			// Save SummonNotification again with the updated applicationNo
			savedNotification = summonNotificationRepository.save(savedNotification);
			log.debug("Saved SummonNotification entity with updated Application No: {}", savedNotification);
			SummonNotificationDTO responseDTO = summonNotificationMapper.toDTO(savedNotification);
			// Create ApplicationInstanceDTO for external microservice
			ApplicationInstanceDTO applicationInstanceDTO = ApplicationInstanceDTO.builder()
					.applicationId(summonNotificationDTO.getApplicationId()) // From frontend
					.applicationNo(savedNotification.getApplicationNo()) // Saved in SummonNotification
					.currentStatus(savedNotification.getStatus()) // From SummonNotification's status
					.documentDate(savedNotification.getSummonIssuanceDate())
					.publishedDocumentUrl(summonNotificationDTO.getPublishedDocumentUrl())
					// .lokSabhaSession(summonNotificationDTO.getLokSabhaNo())
					// .lokSabhaSessionNumber(summonNotificationDTO.getSessionNo())
					// .rajyaSabhaSession(summonNotificationDTO.getRajyasabhaSessionNo())
					// Optional from frontend
					.refId(savedNotification.getId()) // Use SummonNotification's ID as refId
					.build();

			if (houseId == 1) {
				MastersRajyaSabha session = mastersRSSRepository.findById(serialNumberRequest.getSessionId()).get();
				applicationInstanceDTO.setRajyaSabhaSession(session.getSessionNumber());
			}
			if (houseId == 2) {
				MastersLokSabhaSession session = masterLSSRepository.findById(serialNumberRequest.getSessionId()).get();
				applicationInstanceDTO.setLokSabhaSessionNumber(session.getSessionNumber());
				applicationInstanceDTO.setLokSabhaSession(session.getLokSabha().getLokSabhaNumber());
			}
			log.debug("Created ApplicationInstanceDTO for microservice call: {}", applicationInstanceDTO);
			// Call ApplicationInstance microservice to save the application instance data
			ApplicationInstanceDTO savedInstanceDTO = createApplicationInstance(applicationInstanceDTO);
			log.debug("Saved ApplicationInstanceDTO returned from microservice: {}", savedInstanceDTO);
			if (savedInstanceDTO == null) {
				log.error("ApplicationInstanceDTO is null after the microservice call!");
				throw new RuntimeException("ApplicationInstance creation failed");
			}
			Long applicationInstanceId = savedInstanceDTO.getId();
			log.debug("ApplicationInstance ID retrieved: {}", applicationInstanceId);
			if (summonNotificationDTO.getTemplateId() != null) {
				validateTemplateId(summonNotificationDTO.getTemplateId());
			}
			// Create ApplicationPapersDTO and save template content from SummonNotification
			ApplicationPapersDTO applicationPapersDTO = ApplicationPapersDTO.builder()
					.applicationId(summonNotificationDTO.getApplicationId())
					.applicationInstanceId(applicationInstanceId).templateId(summonNotificationDTO.getTemplateId())
					.templateContent(summonNotificationDTO.getTemplateContent())
					.templateHindiId(summonNotificationDTO.getTemplateHindiId())
					.templateContentHindi(summonNotificationDTO.getTemplateContentHindi()).build();
			log.debug("Created ApplicationPapersDTO: {}", applicationPapersDTO);
			// Save ApplicationPapers data
			applicationPapersService.saveApplicationPapers(applicationPapersDTO);
			log.debug("Saved ApplicationPapers data.");
			// Convert saved entity back to DTO
			responseDTO.setApplicationInstanceDTO(savedInstanceDTO);
			responseDTO.setTemplateId(summonNotificationDTO.getTemplateId());
			responseDTO.setTemplateHindiId(summonNotificationDTO.getTemplateHindiId());
			log.debug("Returning SummonNotificationDTO with ApplicationInstanceDTO and Template ID: {}", responseDTO);
			return responseDTO;
		} catch (ResourceNotFoundException e) {
			log.error("Resource not found during saveSummonNotification: {}", e.getMessage());
			throw e; // Re-throw the exception for further handling
		} catch (Exception e) {
			log.error("Error in save Summon Notification: ", e);
			throw new RuntimeException("Failed to save Summon Notification", e);
		}
	}

	private String generateApplicationNo(String lokSabhaNo, String sessionNo, String rajyaSabhaSessionNo, Long id) {
		if (lokSabhaNo != null && !lokSabhaNo.isEmpty() && sessionNo != null && !sessionNo.isEmpty()) {
			return "SN-LS" + "-" + lokSabhaNo + "-" + sessionNo + "-" + id;
		} else if (rajyaSabhaSessionNo != null && !rajyaSabhaSessionNo.isEmpty()) {
			return "SN-RS" + "-" + rajyaSabhaSessionNo + "-" + id;
		} else {
			throw new IllegalArgumentException("Insufficient data to generate application number");
		}
	}

	private ApplicationInstanceDTO createApplicationInstance(ApplicationInstanceDTO applicationInstanceDTO) {
		String url = applicationInstanceServiceUrl + "/create";
		log.debug("Calling ApplicationInstance service at URL: {} with ApplicationInstanceDTO: {}", url,
				applicationInstanceDTO);

		try {
			ResponseEntity<GenericResponse<ApplicationInstanceDTO>> response = restTemplate.exchange(url,
					HttpMethod.POST, new HttpEntity<>(applicationInstanceDTO),
					new ParameterizedTypeReference<GenericResponse<ApplicationInstanceDTO>>() {
					});

			if (response == null || response.getBody() == null || response.getBody().getData() == null) {
				log.error("Response or body is null from ApplicationInstance service");
				throw new RuntimeException("Failed to create ApplicationInstance, response is null");
			}

			ApplicationInstanceDTO savedInstance = response.getBody().getData();
			log.debug("Received ApplicationInstanceDTO from ApplicationInstance service: {}", savedInstance);
			return savedInstance;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found when calling ApplicationInstance service: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error calling ApplicationInstance service: ", e);
			throw new RuntimeException("Failed to create ApplicationInstance", e);
		}
	}

	private TemplatesDTO validateTemplateId(Long templateId) {
		String masterServiceUrl = templateServiceUrl + "/getById/" + templateId; // Use '/' to separate URL segments
		ResponseEntity<TemplatesDTO> response = restTemplate.getForEntity(masterServiceUrl, TemplatesDTO.class);

		if (!response.getStatusCode().is2xxSuccessful()) {
			log.error("Failed to fetch Template from master service: {} - {}", response.getStatusCode(),
					response.getBody());
			throw new RuntimeException(
					"Failed to fetch Template from master service. Status: " + response.getStatusCode());
		}

		return response.getBody();
	}

	@Override
	public List<SummonNotificationDTO> getAllSummonNotifications() {
		log.debug("Entering getAllSummonNotifications");

		try {
			List<SummonNotification> summonNotifications = summonNotificationRepository.findAll();
			log.debug("Fetched all Summon Notifications from database: {}", summonNotifications);
			Long houseId = null;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof UserDetailsDTO) {
				houseId = ((UserDetailsDTO) principal).getHouseId();
			}
			List<SummonNotificationDTO> responseList = new ArrayList<>();
			for (SummonNotification notification : summonNotifications) {
				if (houseId == 1) {
					if (StringUtils.isBlank(notification.getSessionNo())) {
						// !(notification.getSessionNo().isBlank())) {
						SummonNotificationDTO dto = summonNotificationMapper.toDTO(notification);
						ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(
								notification.getApplicationNo());
						dto.setApplicationInstanceDTO(instanceDTO);
						if (instanceDTO != null && instanceDTO.getId() != null) {
							applicationPapersRepository.findByApplicationInstanceId(instanceDTO.getId())
									.ifPresent(applicationPapers -> {
										dto.setTemplateId(applicationPapers.getTemplateId());
										dto.setTemplateHindiId(applicationPapers.getTemplateHindiId());
									});
						}
						responseList.add(dto);
					}
				} else if (houseId == 2) {
					if (StringUtils.isBlank(notification.getRajyasabhaSessionNo())) {
						SummonNotificationDTO dto = summonNotificationMapper.toDTO(notification);
						ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(
								notification.getApplicationNo());
						dto.setApplicationInstanceDTO(instanceDTO);
						if (instanceDTO != null && instanceDTO.getId() != null) {
							applicationPapersRepository.findByApplicationInstanceId(instanceDTO.getId())
									.ifPresent(applicationPapers -> {
										dto.setTemplateId(applicationPapers.getTemplateId());
										dto.setTemplateHindiId(applicationPapers.getTemplateHindiId());
									});
						}
						responseList.add(dto);
					}
				}
			}
			log.debug("Returning all SummonNotificationDTOs with ApplicationInstanceDTOs: {}", responseList);
			return responseList;
		} catch (Exception e) {
			log.error("Error in getAllSummonNotifications: ", e);
			throw new RuntimeException("Failed to retrieve Summon Notifications", e);
		}
	}

	@Override
	public SummonNotificationDTO getSummonNotificationById(Long id) {
		log.debug("Entering getSummonNotificationById with ID: {}", id);

		try {
			// Find the SummonNotification entity by ID or throw an exception if not found
			SummonNotification summonNotification = summonNotificationRepository.findById(id).orElseThrow(() -> {
				log.error("SummonNotification not found with ID: {}", id);
				return new ResourceNotFoundException("Summon Notification not found with id: " + id);
			});

			// Convert the SummonNotification entity to a DTO
			SummonNotificationDTO summonNotificationDTO = summonNotificationMapper.toDTO(summonNotification);

			// Fetch ApplicationInstanceDTO using the applicationNo from SummonNotification
			ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(summonNotification.getApplicationNo());
			summonNotificationDTO.setApplicationInstanceDTO(instanceDTO);

			// Retrieve ApplicationPapers by applicationInstanceId
			if (instanceDTO != null && instanceDTO.getId() != null) {
				applicationPapersRepository.findByApplicationInstanceId(instanceDTO.getId())
						.ifPresent(applicationPapers -> {
							summonNotificationDTO.setTemplateId(applicationPapers.getTemplateId());
							summonNotificationDTO.setTemplateHindiId(applicationPapers.getTemplateHindiId());
						});
			}
			log.debug("Returning SummonNotificationDTO: {}", summonNotificationDTO);
			return summonNotificationDTO;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found when retrieving Summon Notification: {}", e.getMessage());
			throw e; // Re-throw the exception for further handling
		} catch (Exception e) {
			log.error("Error in getSummonNotificationById: ", e);
			throw new RuntimeException("Failed to retrieve Summon Notification by ID", e);
		}
	}
	private ApplicationInstanceDTO fetchApplicationInstanceData(String applicationNo) {
		String url = applicationInstanceServiceUrl + "/getApplicationNo/" + applicationNo;
		
		log.debug("Fetching ApplicationInstance by applicationNo from URL: {}", url);
		try {

			ResponseEntity<GenericResponse<ApplicationInstanceDTO>> response = restTemplate.exchange(url,
					HttpMethod.GET, null, new ParameterizedTypeReference<GenericResponse<ApplicationInstanceDTO>>() {
					});
			if (response == null || response.getBody() == null || response.getBody().getData() == null) {
				log.error("Response or body is null when fetching ApplicationInstance for applicationNo: {}",
						applicationNo);
				throw new RuntimeException("Failed to fetch ApplicationInstance, response is null");
			}
			
			ApplicationInstanceDTO instanceDTO = response.getBody().getData();
			log.debug("Fetched ApplicationInstanceDTO: {}", instanceDTO);
			return instanceDTO;
		} catch (ResourceNotFoundException e) {
			log.error("Resource not found for applicationNo: {}", applicationNo);
			throw e; // Re-throw the exception for further handling
		} catch (Exception e) {
			log.error("Error fetching ApplicationInstance for applicationNo: {}", applicationNo, e);
			throw new RuntimeException("Failed to fetch ApplicationInstance data", e);
		}
	}

	@Transactional
	@Override
	public SummonNotificationDTO updateSummonNotification(Long id, SummonNotificationDTO summonNotificationDTO) {
		log.debug("Starting update for SummonNotification with ID: {}", id);

		try {
			// Fetch the existing SummonNotification
			SummonNotification existingNotification = summonNotificationRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("SummonNotification not found with id: " + id));

			// Update only the fields you want to change
			existingNotification.setNotificationNo(summonNotificationDTO.getNotificationNo());
			existingNotification.setStatus(summonNotificationDTO.getStatus());
			existingNotification.setSummonStartAt(summonNotificationDTO.getSummonStartAt());
			existingNotification.setSummonIssuanceDate(summonNotificationDTO.getSummonIssuanceDate());
			existingNotification.setSummonIssuanceDateHindi(summonNotificationDTO.getSummonIssuanceDateHindi());
			existingNotification.setSummonIssuanceDateHindiEn(summonNotificationDTO.getSummonIssuanceDateHindiEn());
			existingNotification.setTemplateContent(summonNotificationDTO.getTemplateContent());
			existingNotification.setTemplateContentHindi(summonNotificationDTO.getTemplateContentHindi());

			// Save the updated SummonNotification
			SummonNotification updatedNotification = summonNotificationRepository.save(existingNotification);

			// Prepare the response DTO
			SummonNotificationDTO responseDTO = summonNotificationMapper.toDTO(updatedNotification);

			// Now, let's automatically update the ApplicationInstance
			if (updatedNotification.getStatus() != null || updatedNotification.getSummonIssuanceDate() != null) {
				ApplicationInstanceDTO applicationInstanceDTO = new ApplicationInstanceDTO();

				if (updatedNotification.getStatus() != null) {
					applicationInstanceDTO.setCurrentStatus(updatedNotification.getStatus());
					log.debug("Setting currentStatus: {}", updatedNotification.getStatus());
				}

				if (updatedNotification.getSummonIssuanceDate() != null) {
					applicationInstanceDTO.setDocumentDate(updatedNotification.getSummonIssuanceDate());
					log.debug("Setting documentDate: {}", updatedNotification.getSummonIssuanceDate());
				}

				String url = applicationInstanceServiceUrl + "/updateByApplicationNo/"
						+ updatedNotification.getApplicationNo();
				try {
					log.debug("Calling external service at {} with DTO: {}", url, applicationInstanceDTO);
					restTemplate.put(url, applicationInstanceDTO);
					log.debug("Successfully updated ApplicationInstance for applicationNo: {}",
							updatedNotification.getApplicationNo());
				} catch (Exception e) {
					log.error("Failed to update ApplicationInstance: ", e);
					throw new RuntimeException("Failed to update ApplicationInstance", e);
				}
			} else {
				log.warn("No valid fields to update for ApplicationInstance.");
			}

			// Fetch the updated ApplicationInstance data
			String applicationNo = updatedNotification.getApplicationNo(); // Get the applicationNo for the
																			// ApplicationInstance
			String applicationInstanceUrl = applicationInstanceServiceUrl + "/getApplicationNo/" + applicationNo;

			try {
				ResponseEntity<GenericResponse<ApplicationInstanceDTO>> response = restTemplate.exchange(
						applicationInstanceUrl, HttpMethod.GET, null,
						new ParameterizedTypeReference<GenericResponse<ApplicationInstanceDTO>>() {
						});

				if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
					ApplicationInstanceDTO applicationInstanceDTOResponse = response.getBody().getData();
					if (applicationInstanceDTOResponse != null) {
						responseDTO.setApplicationInstanceDTO(applicationInstanceDTOResponse); // Include
																								// ApplicationInstance
																								// data
						responseDTO.setApplicationId(applicationInstanceDTOResponse.getApplicationId()); // Include
																											// applicationId
						log.debug("Fetched ApplicationInstance for applicationNo: {}", applicationNo);
					}
				}
			} catch (Exception e) {
				log.error("Failed to fetch ApplicationInstance: ", e);
				// You can choose to handle this case as needed
			}

			if (summonNotificationDTO.getTemplateContent() != null
					|| summonNotificationDTO.getTemplateContentHindi() != null) {
				Optional<ApplicationPapers> existingPapersOpt = applicationPapersRepository
						.findByApplicationIdAndApplicationInstanceId(summonNotificationDTO.getApplicationId(),
								responseDTO.getApplicationInstanceDTO().getId());

				if (existingPapersOpt.isPresent()) {
					ApplicationPapers existingPapers = existingPapersOpt.get();
					existingPapers.setTemplateContent(summonNotificationDTO.getTemplateContent());
					existingPapers.setTemplateContentHindi(summonNotificationDTO.getTemplateContentHindi());

					applicationPapersRepository.save(existingPapers);
					log.debug("Updated ApplicationPapers with new template content.");
				} else {
					log.warn("ApplicationPapers not found for ApplicationId: {} and ApplicationInstanceId: {}",
							summonNotificationDTO.getApplicationId(), responseDTO.getApplicationInstanceDTO().getId());
				}
			}

			log.debug("Returning updated SummonNotificationDTO with ApplicationInstance data: {}", responseDTO);
			return responseDTO;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found during updateSummonNotification: {}", e.getMessage());
			throw e; // Re-throw the exception for further handling
		} catch (Exception e) {
			log.error("Error in updateSummonNotification: ", e);
			throw new RuntimeException("Failed to update SummonNotification", e);
		}
	}

	@Override
	public List<SummonNotificationDTO> getAllSummonNotificationBySessionNo(String sessionNo) {
		log.debug("Entering getSummonNotificationBySessionNo with sessionNo: {}", sessionNo);

		try {
			List<SummonNotification> summonNotifications = summonNotificationRepository.findBySessionNo(sessionNo);

			if (summonNotifications.isEmpty()) {
				log.error("No SummonNotifications found with sessionNo: {}", sessionNo);
				throw new ResourceNotFoundException("No SummonNotifications found with sessionNo: " + sessionNo);
			}

			// Convert each SummonNotification entity to SummonNotificationDTO
			List<SummonNotificationDTO> summonNotificationDTOs = summonNotifications.stream()
					.map(summonNotification -> {
						SummonNotificationDTO dto = summonNotificationMapper.toDTO(summonNotification);
						ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(
								summonNotification.getApplicationNo());
						dto.setApplicationInstanceDTO(instanceDTO);
						if (instanceDTO != null && instanceDTO.getId() != null) {
							applicationPapersRepository.findByApplicationInstanceId(instanceDTO.getId())
									.ifPresent(applicationPapers -> {
										dto.setTemplateId(applicationPapers.getTemplateId());
										dto.setTemplateHindiId(applicationPapers.getTemplateHindiId());
									});
						}
						return dto;
					}).collect(Collectors.toList());

			log.debug("Returning list of SummonNotificationDTOs: {}", summonNotificationDTOs);
			return summonNotificationDTOs;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found when retrieving SummonNotifications: {}", e.getMessage());
			throw e; // Re-throw the exception for further handling
		} catch (Exception e) {
			log.error("Error in getAllSummonNotificationBySessionNo: ", e);
			throw new RuntimeException("Failed to retrieve SummonNotifications by sessionNo", e);
		}
	}

	@Transactional
	@Override
	public void deleteSummonNotification(Long id) {
		log.debug("Starting deleteSummonNotification with ID: {}", id);

		// Fetch the SummonNotification or throw a ResourceNotFoundException
		SummonNotification summonNotification = summonNotificationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("SummonNotification not found with id: " + id));

		// Try deleting the ApplicationInstance by calling the external microservice
		String url = applicationInstanceServiceUrl + "/deleteByApplicationNo/" + summonNotification.getApplicationNo();
		try {
			restTemplate.delete(url); // Call to delete ApplicationInstance
			log.debug("Deleted ApplicationInstance associated with refId: {}", summonNotification.getApplicationNo());

			// Mark the SummonNotification as deleted
			summonNotification.setIsDeleted(true);
			summonNotificationRepository.save(summonNotification); // Save the updated entity
			log.debug("Soft deleted SummonNotification with ID: {}", id);

		} catch (HttpClientErrorException.NotFound e) {
			log.error("ApplicationInstance not found for deletion: {}", e.getMessage());
			throw new ResourceNotFoundException(
					"Failed to delete ApplicationInstance with refId: " + summonNotification.getApplicationNo());
		} catch (HttpClientErrorException e) {
			log.error("Failed to delete ApplicationInstance: {}", e.getMessage());
			throw new ExternalServiceException("Failed to delete ApplicationInstance", e);
		} catch (Exception e) {
			log.error("Unexpected error in deleteSummonNotification: ", e);
			throw new RuntimeException("Failed to delete SummonNotification", e);
		}
	}

}
