package com.dsp.agendaPapers.prorogationnotice.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.web.client.RestTemplate;

import com.dsp.agendaPapers.applicationPapers.dto.ApplicationPapersDTO;
import com.dsp.agendaPapers.applicationPapers.dto.TemplatesDTO;
import com.dsp.agendaPapers.applicationPapers.entity.ApplicationPapers;
import com.dsp.agendaPapers.applicationPapers.repository.ApplicationPapersRepository;
import com.dsp.agendaPapers.applicationPapers.service.ApplicationPapersService;
import com.dsp.agendaPapers.dto.UserDetailsDTO;
import com.dsp.agendaPapers.entity.MastersLokSabhaSession;
import com.dsp.agendaPapers.entity.MastersRajyaSabha;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import com.dsp.agendaPapers.prorogationnotice.Dto.ProrogationNoticeDTO;
import com.dsp.agendaPapers.prorogationnotice.entity.ProrogationNotice;
import com.dsp.agendaPapers.prorogationnotice.mapper.ProrogationNoticeMapper;
import com.dsp.agendaPapers.prorogationnotice.repository.ProrogationNoticeRepository;
import com.dsp.agendaPapers.prorogationnotice.service.ProrogationNoticeService;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberRequest;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberResponse;
import com.dsp.agendaPapers.provisionalcalendarfinal.feignclient.BearerClient;
import com.dsp.agendaPapers.repository.MasterLSSRepository;
import com.dsp.agendaPapers.repository.MastersRSSRepository;
import com.dsp.agendaPapers.response.GenericResponse;
import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;

@Service
public class ProrogationNoticeServiceImpl implements ProrogationNoticeService {

	@Autowired
	private MastersRSSRepository mastersRSSRepository;

	@Autowired
	private MasterLSSRepository masterLSSRepository;
	@Autowired
	private ProrogationNoticeRepository prorogationNoticeRepository;

	@Autowired
	private ApplicationPapersService applicationPapersService;

	@Autowired
	private ProrogationNoticeMapper prorogationNoticeMapper;

	@Value("${application.instance.service.url}")
	private String applicationInstanceServiceUrl;
	@Autowired
	private BearerClient bearerClient;
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApplicationPapersRepository applicationPapersRepository;

	@Value("${template.service.url}") // URL to validate template ID
	private String templateServiceUrl;

	private static final Logger log = LoggerFactory.getLogger(ProrogationNoticeServiceImpl.class);

	@Transactional
	@Override
	public ProrogationNoticeDTO saveProrogationNotice(ProrogationNoticeDTO prorogationNoticeDTO) {
		System.out.println("pppppppppppppppppppppp");
		System.out.println(prorogationNoticeDTO);
		log.debug("Entering saveProrogationNotice with ProrogationNoticeDTO: {}", prorogationNoticeDTO);
		Long houseId = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsDTO) {
			houseId = ((UserDetailsDTO) principal).getHouseId();
		}
		SerialNumberRequest serialNumberRequest = new SerialNumberRequest();
		serialNumberRequest.setAppId(prorogationNoticeDTO.getApplicationId());
		try {
			if (houseId == 1) {
				prorogationNoticeDTO.setLokSabhaNo("");
				prorogationNoticeDTO.setLokSabhaSessionNo("");
				serialNumberRequest.setSessionId(Long.parseLong(prorogationNoticeDTO.getRajyaSabhaSessionNo()));

			} else if (houseId == 2) {
				prorogationNoticeDTO.setRajyaSabhaSessionNo("");
				serialNumberRequest.setSessionId(Long.parseLong(prorogationNoticeDTO.getLokSabhaSessionNo()));
			}
			// Convert DTO to entity
			SerialNumberResponse applicationNo = bearerClient.getResource(serialNumberRequest);
			System.out.println(applicationNo);
			ProrogationNotice prorogationNotice = prorogationNoticeMapper.toEntity(prorogationNoticeDTO);
			log.debug("Converted ProrogationNoticeDTO to entity: {}", prorogationNotice);
			prorogationNotice.setApplicationNo(applicationNo.getId());
			// Save ProrogationNotice entity first to generate the ID
			ProrogationNotice savedNotice = prorogationNoticeRepository.save(prorogationNotice);
			log.debug("First save - Generated ProrogationNotice ID: {}", savedNotice.getId());

			// Generate Application Number using the generated ID
			// String applicationNo = generateApplicationNo(
			// prorogationNoticeDTO.getLokSabhaNo(),
			// prorogationNoticeDTO.getLokSabhaSessionNo(),
			// prorogationNoticeDTO.getRajyaSabhaSessionNo(),
			// savedNotice.getId());
			// savedNotice.setApplicationNo(applicationNo);
			log.debug("Generated Application No: {}", applicationNo);

			// Save ProrogationNotice again with the updated applicationNo
			savedNotice = prorogationNoticeRepository.save(savedNotice);
			log.debug("Saved ProrogationNotice entity with updated Application No: {}", savedNotice);
			ProrogationNoticeDTO responseDTO = prorogationNoticeMapper.toDto(savedNotice);

			// Create ApplicationInstanceDTO for external microservice
			ApplicationInstanceDTO applicationInstanceDTO = ApplicationInstanceDTO.builder()
					.applicationId(prorogationNoticeDTO.getApplicationId()) // From frontend
					.applicationNo(savedNotice.getApplicationNo()) // Saved in ProrogationNotice
					.currentStatus(savedNotice.getStatus()) // From ProrogationNotice's status
					.documentDate(savedNotice.getIssuanceDate())
					.publishedDocumentUrl(prorogationNoticeDTO.getPublishedDocumentUrl()) // Optional from frontend
					.refId(savedNotice.getId())
					// .lokSabhaSession(prorogationNoticeDTO.getLokSabhaNo())
					// .lokSabhaSessionNumber(prorogationNoticeDTO.getLokSabhaSessionNo())
					// .rajyaSabhaSession(prorogationNoticeDTO.getRajyaSabhaSessionNo())
					// Use ProrogationNotice's ID as refId
					.build();
			if (houseId == 1) {
				MastersRajyaSabha session = mastersRSSRepository
						.findById(serialNumberRequest.getSessionId()).get();
				applicationInstanceDTO.setRajyaSabhaSession(session.getSessionNumber());
			}
			if (houseId == 2) {
				MastersLokSabhaSession session = masterLSSRepository
						.findById(serialNumberRequest.getSessionId()).get();
				applicationInstanceDTO.setLokSabhaSessionNumber(session.getSessionNumber());
				applicationInstanceDTO.setLokSabhaSession(session.getLokSabha().getLokSabhaNumber());
			}

			System.out.println("aaaaaaaaaaaaaaaaaaaa");
			System.out.println(applicationInstanceDTO);
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

			// Validate template ID
			validateTemplateId(prorogationNoticeDTO.getTemplateId());

			// Create ApplicationPapersDTO and save template content from ProrogationNotice
			ApplicationPapersDTO applicationPapersDTO = ApplicationPapersDTO.builder()
					.applicationId(prorogationNoticeDTO.getApplicationId())
					.applicationInstanceId(applicationInstanceId)
					.templateId(prorogationNoticeDTO.getTemplateId())
					.templateContent(prorogationNoticeDTO.getTemplateContent())
					.templateHindiId(prorogationNoticeDTO.getTemplateHindiId())
					.templateContentHindi(prorogationNoticeDTO.getTemplateContentHindi())
					.build();
			log.debug("Created ApplicationPapersDTO: {}", applicationPapersDTO);

			// Save ApplicationPapers data
			applicationPapersService.saveApplicationPapers(applicationPapersDTO);
			log.debug("Saved ApplicationPapers data.");

			// Convert saved entity back to DTO
			// ProrogationNoticeDTO responseDTO =
			// prorogationNoticeMapper.toDto(savedNotice);
			responseDTO.setApplicationInstanceDTO(savedInstanceDTO);
			log.debug("Returning ProrogationNoticeDTO with ApplicationInstanceDTO: {}", responseDTO);

			responseDTO.setTemplateId(prorogationNoticeDTO.getTemplateId());
			responseDTO.setTemplateHindiId(prorogationNoticeDTO.getTemplateHindiId());

			log.debug("Returning ProrogationNoticeDTO with ApplicationInstanceDTO and Template ID: {}", responseDTO);

			return responseDTO;
		} catch (ResourceNotFoundException e) {
			log.error("Resource not found during saveProrogationNotice: {}", e.getMessage());
			throw e; // Re-throw the exception for further handling
		} catch (Exception e) {
			log.error("Error in saveProrogationNotice: ", e);
			throw new RuntimeException("Failed to save ProrogationNotice", e);
		}
	}

	private String generateApplicationNo(String lokSabhaNo, String lokSabhaSessionNo, String rajyaSabhaSessionNo,
			Long id) {
		if (lokSabhaNo != null && !lokSabhaNo.isEmpty() && lokSabhaSessionNo != null && !lokSabhaSessionNo.isEmpty()) {
			return "PN-LS" + "-" + lokSabhaNo + "-" + lokSabhaSessionNo + "-" + id;
		} else if (rajyaSabhaSessionNo != null && !rajyaSabhaSessionNo.isEmpty()) {
			return "PN-RS" + "-" + rajyaSabhaSessionNo + "-" + id;
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

		} catch (Exception e) {
			log.error("Error calling ApplicationInstance service: ", e);
			throw new RuntimeException("Failed to create ApplicationInstance", e);
		}
	}

	private void validateTemplateId(Long templateId) {
		String url = templateServiceUrl + "/getById/" + templateId; // Ensure correct URL
		log.debug("Calling master service to validate template ID at URL: {}", url);

		try {
			ResponseEntity<TemplatesDTO> response = restTemplate.getForEntity(url, TemplatesDTO.class);

			if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
				log.error("Failed to fetch Template from master service. Status: {}, Body: {}",
						response.getStatusCode(), response.getBody());
				throw new RuntimeException("Invalid template ID: " + templateId);
			}

			log.debug("Successfully validated template ID: {}", templateId);

		} catch (Exception e) {
			log.error("Error validating template ID: ", e);
			throw new RuntimeException("Failed to validate template ID", e);
		}
	}

	@Override
	public ProrogationNoticeDTO getPropagationNoticeById(Long id) {
		log.debug("Entering getPropagationNoticeById with ID: {}", id);

		try {
			// Find the PropagationNotice entity by ID or throw an exception if not found
			ProrogationNotice propagationNotice = prorogationNoticeRepository.findById(id).orElseThrow(() -> {
				log.error("PropagationNotice not found with ID: {}", id);
				return new ResourceNotFoundException("PropagationNotice not found with id: " + id);
			});

			// Convert the PropagationNotice entity to a DTO
			ProrogationNoticeDTO propagationNoticeDTO = prorogationNoticeMapper.toDto(propagationNotice);

			// Fetch ApplicationInstanceDTO using the applicationNo from PropagationNotice
			ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(propagationNotice.getApplicationNo());
			propagationNoticeDTO.setApplicationInstanceDTO(instanceDTO);

			// Retrieve ApplicationPapers by applicationInstanceId
			if (instanceDTO != null && instanceDTO.getId() != null) {
				applicationPapersRepository.findByApplicationInstanceId(instanceDTO.getId())
						.ifPresent(applicationPapers -> {
							propagationNoticeDTO.setTemplateId(applicationPapers.getTemplateId());
							propagationNoticeDTO.setTemplateHindiId(applicationPapers.getTemplateHindiId());
						});
			}
			log.debug("Returning PropagationNoticeDTO: {}", propagationNoticeDTO);
			return propagationNoticeDTO;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found when retrieving PropagationNotice: {}", e.getMessage());
			throw e; // Re-throw the exception for further handling
		} catch (Exception e) {
			log.error("Error in getPropagationNoticeById: ", e);
			throw new RuntimeException("Failed to retrieve PropagationNotice by ID", e);
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

	@Override
	public List<ProrogationNoticeDTO> getAllProrogationNoticeBySessionNo(String lokSabhaSessionNo) {
		log.debug("Entering getProrogationNoticeBySessionNo with sessionNo: {}", lokSabhaSessionNo);
		try {
			List<ProrogationNotice> prorogationNotices = prorogationNoticeRepository
					.findByLokSabhaSessionNo(lokSabhaSessionNo);

			if (prorogationNotices.isEmpty()) {
				log.error("No ProrogationNotices found with sessionNo: {}", lokSabhaSessionNo);
				throw new ResourceNotFoundException("No ProrogationNotices found with sessionNo: " + lokSabhaSessionNo);
			}

			// Convert each ProrogationNotice entity to ProrogationNoticeDTO
			List<ProrogationNoticeDTO> prorogationNoticeDTOs = prorogationNotices.stream()
					.map(prorogationNotice -> {
						ProrogationNoticeDTO dto = prorogationNoticeMapper.toDto(prorogationNotice);
						ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(
								prorogationNotice.getApplicationNo());
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

			log.debug("Returning list of ProrogationNoticeDTOs: {}", prorogationNoticeDTOs);
			return prorogationNoticeDTOs;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found when retrieving ProrogationNotices: {}", e.getMessage());
			throw e; // Re-throw the exception for further handling
		} catch (Exception e) {
			log.error("Error in getAllProrogationNoticeBySessionNo: ", e);
			throw new RuntimeException("Failed to retrieve ProrogationNotices by sessionNo", e);
		}
	}

	@Transactional
	@Override
	public ProrogationNoticeDTO updateProrogationNotice(Long id, ProrogationNoticeDTO prorogationNoticeDTO) {
		log.debug("Starting update for ProrogationNotice with ID: {}", id);

		try {
			// Fetch the existing ProrogationNotice
			ProrogationNotice existingNotice = prorogationNoticeRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("ProrogationNotice not found with id: " + id));

			// Update only the fields you want to change
			existingNotice.setNotificationNo(prorogationNoticeDTO.getNotificationNo());
			existingNotice.setStatus(prorogationNoticeDTO.getStatus());
			existingNotice.setIssuanceDate(prorogationNoticeDTO.getIssuanceDate());
			existingNotice.setIssuanceDateSakaEra(prorogationNoticeDTO.getIssuanceDateSakaEra());
			existingNotice.setIssuanceDateSakaEraHindi(prorogationNoticeDTO.getIssuanceDateSakaEraHindi());
			existingNotice.setTemplateContent(prorogationNoticeDTO.getTemplateContent());
			existingNotice.setTemplateContentHindi(prorogationNoticeDTO.getTemplateContentHindi());

			// Save the updated ProrogationNotice
			ProrogationNotice updatedNotice = prorogationNoticeRepository.save(existingNotice);

			// Prepare the response DTO
			ProrogationNoticeDTO responseDTO = prorogationNoticeMapper.toDto(updatedNotice);

			// Automatically update the ApplicationInstance
			if (updatedNotice.getStatus() != null || updatedNotice.getIssuanceDate() != null) {
				ApplicationInstanceDTO applicationInstanceDTO = new ApplicationInstanceDTO();

				// Set fields conditionally
				if (updatedNotice.getStatus() != null) {
					applicationInstanceDTO.setCurrentStatus(updatedNotice.getStatus());
					log.debug("Setting currentStatus: {}", updatedNotice.getStatus());
				}
				if (updatedNotice.getIssuanceDate() != null) {
					applicationInstanceDTO.setDocumentDate(updatedNotice.getIssuanceDate());
					log.debug("Setting documentDate: {}", updatedNotice.getIssuanceDate());
				}

				// Call the external service to update ApplicationInstance
				String url = applicationInstanceServiceUrl + "/updateByApplicationNo/"
						+ updatedNotice.getApplicationNo();
				try {
					log.debug("Calling external service at {} with DTO: {}", url, applicationInstanceDTO);
					restTemplate.put(url, applicationInstanceDTO);
					log.debug("Updated ApplicationInstance with applicationNo: {}", updatedNotice.getApplicationNo());
				} catch (Exception e) {
					log.error("Failed to update ApplicationInstance for applicationNo: {}",
							updatedNotice.getApplicationNo(), e);
					throw new RuntimeException("Failed to update ApplicationInstance", e);
				}
			} else {
				log.warn("No valid fields to update for ApplicationInstance with applicationNo: {}",
						updatedNotice.getApplicationNo());
			}

			// Fetch the updated ApplicationInstance data
			String applicationNo = updatedNotice.getApplicationNo();
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
				// Handle as needed
			}

			if (prorogationNoticeDTO.getTemplateContent() != null
					|| prorogationNoticeDTO.getTemplateContentHindi() != null) {
				Optional<ApplicationPapers> existingPapersOpt = applicationPapersRepository
						.findByApplicationIdAndApplicationInstanceId(prorogationNoticeDTO.getApplicationId(),
								responseDTO.getApplicationInstanceDTO().getId());

				if (existingPapersOpt.isPresent()) {
					ApplicationPapers existingPapers = existingPapersOpt.get();
					existingPapers.setTemplateContent(prorogationNoticeDTO.getTemplateContent());
					existingPapers.setTemplateContentHindi(prorogationNoticeDTO.getTemplateContentHindi());

					applicationPapersRepository.save(existingPapers);
					log.debug("Updated ApplicationPapers with new template content.");
				} else {
					log.warn("ApplicationPapers not found for ApplicationId: {} and ApplicationInstanceId: {}",
							prorogationNoticeDTO.getApplicationId(), responseDTO.getApplicationInstanceDTO().getId());
				}
			}

			log.debug("Returning updated ProrogationNoticeDTO with ApplicationInstance data: {}", responseDTO);
			return responseDTO;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found during updateProrogationNotice: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error in updateProrogationNotice: ", e);
			throw new RuntimeException("Failed to update ProrogationNotice", e);
		}
	}

}
