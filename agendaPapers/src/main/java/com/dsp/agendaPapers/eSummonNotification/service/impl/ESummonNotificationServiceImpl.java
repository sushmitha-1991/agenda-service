package com.dsp.agendaPapers.eSummonNotification.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.dsp.agendaPapers.applicationPapers.dto.ApplicationPapersDTO;
import com.dsp.agendaPapers.applicationPapers.dto.TemplatesDTO;
import com.dsp.agendaPapers.applicationPapers.entity.ApplicationPapers;
import com.dsp.agendaPapers.applicationPapers.repository.ApplicationPapersRepository;
import com.dsp.agendaPapers.applicationPapers.service.ApplicationPapersService;
import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonNotificationRepository;
import com.dsp.agendaPapers.eSummonNotification.service.ESummonNotificationService;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import com.dsp.agendaPapers.mapper.ESummonNotificationMapper;
import com.dsp.agendaPapers.response.GenericResponse;
import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;
import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;
import com.dsp.agendaPapers.summonNotification.entity.SummonNotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ESummonNotificationServiceImpl implements ESummonNotificationService {

	private static final Logger log = LoggerFactory.getLogger(ESummonNotificationServiceImpl.class);

	@Autowired
	private ESummonNotificationRepository eSummonNotificationRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApplicationPapersService applicationPapersService;

	@Value("${template.service.url}") // URL to validate template ID
	private String templateServiceUrl;

	@Value("${application.instance.service.url}")
	private String applicationInstanceServiceUrl;
	
	@Autowired
	private ApplicationPapersRepository applicationPapersRepository;

	

	@Transactional
	@Override
	public ESummonNotificationDTO saveESummonNotification(ESummonNotificationDTO eSummonNotificationDTO) {
		log.debug("Entering saveESummonNotification with ESummonNotificationDTO: {}", eSummonNotificationDTO);

		try {
			// Convert DTO to entity
			ESummonNotification summonNotification = ESummonNotificationMapper.toEntity(eSummonNotificationDTO);
			log.debug("Converted E-SummonNotificationDTO to entity: {}", summonNotification);
             System.out.println("@@@@@@@@@@@@@" + summonNotification);
			// Save SummonNotification entity initially to generate the ID
			ESummonNotification savedNotification = eSummonNotificationRepository.save(summonNotification);
			log.debug("Initial save of E-SummonNotification to generate ID: {}", savedNotification);

			 String applicationNo = generateApplicationNo(
					 eSummonNotificationDTO.getLokSabhaNo(),
					 eSummonNotificationDTO.getSessionNo(),
					 eSummonNotificationDTO.getRajyasabhaSessionNo(),
			            savedNotification.getId()
			        );
			savedNotification.setApplicationNo(applicationNo);
			log.debug("Generated Application No: {}", applicationNo);

			// Save the SummonNotification entity again with the generated Application No
			savedNotification = eSummonNotificationRepository.save(savedNotification);
			log.debug("Saved E-SummonNotification entity with Application No: {}", savedNotification);

			// Log saved attributes
			log.info(
					"Saved E-SummonNotification attributes: ID={}, LokSabhaNo={}, SessionNo={}, SummonIssuanceDate={}, Status={}, TemplateContent={}",
					savedNotification.getId(), savedNotification.getLokSabhaNo(), savedNotification.getSessionNo(),
					savedNotification.getEsummonIssuanceDate(), savedNotification.getStatus(),
					savedNotification.getTemplateContent());

			// Create ApplicationInstanceDTO to send to the ApplicationInstance microservice
			ApplicationInstanceDTO applicationInstanceDTO = ApplicationInstanceDTO.builder()
					.applicationId(eSummonNotificationDTO.getApplicationId()) // From frontend
					.applicationNo(savedNotification.getApplicationNo()) // Saved in SummonNotification
					.currentStatus(savedNotification.getStatus()) // From frontend
					.documentDate(savedNotification.getEsummonIssuanceDate())
					.publishedDocumentUrl(eSummonNotificationDTO.getPublishedDocumentUrl()) // From frontend
					.refId(savedNotification.getId()) // Use SummonNotification's ID as refId
					.build();
			log.debug("Created ApplicationInstanceDTO for microservice call: {}", applicationInstanceDTO);

			// Call ApplicationInstance microservice to save the application instance data
			ApplicationInstanceDTO savedInstanceDTO = createApplicationInstance(applicationInstanceDTO);
			log.debug("Saved ApplicationInstanceDTO returned from microservice: {}", savedInstanceDTO);

			// Validate template ID
			if (eSummonNotificationDTO.getTemplateId() != null) {
			validateTemplateId(eSummonNotificationDTO.getTemplateId());
			}
			if (eSummonNotificationDTO.getTemplateHindiId() != null) {
			validateTemplateId(eSummonNotificationDTO.getTemplateHindiId());
			}
			// Create ApplicationPapersDTO and save template content from SummonNotification
			ApplicationPapersDTO applicationPapersDTO = ApplicationPapersDTO.builder()
					.applicationId(eSummonNotificationDTO.getApplicationId())
					.applicationInstanceId(savedInstanceDTO.getId())
					.templateId(eSummonNotificationDTO.getTemplateId())
					.templateContent(eSummonNotificationDTO.getTemplateContent())
					.templateHindiId(eSummonNotificationDTO.getTemplateHindiId())
					.templateContentHindi(eSummonNotificationDTO.getTemplateContentHindi()).build();
			log.debug("Created ApplicationPapersDTO: {}", applicationPapersDTO);

			// Save ApplicationPapers data
			applicationPapersService.saveApplicationPapers(applicationPapersDTO);
			log.debug("Saved ApplicationPapers data.");

			// Convert saved entity back to DTO
			ESummonNotificationDTO responseDTO = ESummonNotificationMapper.toDTO(savedNotification);
			responseDTO.setApplicationInstanceDTO(savedInstanceDTO); // Set the ApplicationInstanceDTO
			log.debug("Returning E-SummonNotificationDTO with ApplicationInstanceDTO: {}", responseDTO);

			 responseDTO.setTemplateId(eSummonNotificationDTO.getTemplateId()); 
			 responseDTO.setTemplateHindiId(eSummonNotificationDTO.getTemplateHindiId());

	            log.debug("Returning E-SummonNotificationDTO with ApplicationInstanceDTO and Template ID: {}", responseDTO);

			return responseDTO;
		} catch (Exception e) {
			log.error("Error in saveESummonNotification: ", e);
			throw e;
		}
	}

	private String generateApplicationNo(String lokSabhaNo, String sessionNo, String rajyaSabhaSessionNo, Long id) {
	    if (lokSabhaNo != null && !lokSabhaNo.isEmpty() && sessionNo != null && !sessionNo.isEmpty()) {
	        return "ESLS" + lokSabhaNo + sessionNo + id;
	    } else if (rajyaSabhaSessionNo != null && !rajyaSabhaSessionNo.isEmpty()) {
	        return "ESRS" + rajyaSabhaSessionNo + id;
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
			throw e; // Re-throw the exception for further handling
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
	public List<ESummonNotificationDTO> getAllESummonNotifications() {
		log.debug("Entering getAll E-SummonNotifications");
		List<ESummonNotification> notifications = eSummonNotificationRepository.findAll();
		log.debug("Fetched all E-SummonNotifications: {}", notifications);

		List<ESummonNotificationDTO> notificationDTOs = new ArrayList<>();

		for (ESummonNotification notification : notifications) {
			log.debug("Processing E-SummonNotification: {}", notification);
			ESummonNotificationDTO dto = ESummonNotificationMapper.toDTO(notification);
			ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(notification.getApplicationNo());
			dto.setApplicationInstanceDTO(instanceDTO);
			
			 if (instanceDTO != null && instanceDTO.getId() != null) {
	                applicationPapersRepository.findByApplicationInstanceId(instanceDTO.getId())
	                        .ifPresent(applicationPapers -> {
	                            dto.setTemplateId(applicationPapers.getTemplateId());
	                            dto.setTemplateHindiId(applicationPapers.getTemplateHindiId());
	                        });
	            }
			notificationDTOs.add(dto);
		}

		log.debug("Returning list of E-SummonNotificationDTOs: {}", notificationDTOs);
		return notificationDTOs;
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
	public ESummonNotificationDTO getESummonNotificationById(Long id) {
		log.debug("Entering getE-SummonNotificationById with id: {}", id);

		ESummonNotification notification = eSummonNotificationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("e SummonNotification not found with id: " + id));
		log.debug("Fetched E-SummonNotification by id: {}", notification);

		ESummonNotificationDTO dto = ESummonNotificationMapper.toDTO(notification);
		ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(notification.getApplicationNo());
		dto.setApplicationInstanceDTO(instanceDTO);
		
		 if (instanceDTO != null && instanceDTO.getId() != null) {
             applicationPapersRepository.findByApplicationInstanceId(instanceDTO.getId())
                     .ifPresent(applicationPapers -> {
                         dto.setTemplateId(applicationPapers.getTemplateId());
                         dto.setTemplateHindiId(applicationPapers.getTemplateHindiId());
                     });
         }

		log.debug("Returning E-SummonNotificationDTO with ApplicationInstanceDTO: {}", dto);
		return dto;
	}

	@Override
	public List<ESummonNotificationDTO> getAllESummonNotificationBySessionNo(String sessionNo) {
		log.debug("Entering get All E-SummonNotificationBySessionNo with sessionNo: {}", sessionNo);

		try {
			// Fetch all ESummonNotifications with the provided sessionNo
			List<ESummonNotification> eSummonNotifications = eSummonNotificationRepository.findBySessionNo(sessionNo);

			if (eSummonNotifications.isEmpty()) {
				log.error("No E-SummonNotifications found with sessionNo: {}", sessionNo);
				throw new ResourceNotFoundException("No E-SummonNotifications found with sessionNo: " + sessionNo);
			}

			// Convert each ESummonNotification entity to ESummonNotificationDTO
			List<ESummonNotificationDTO> eSummonNotificationDTOs = eSummonNotifications.stream()
					.map(summonNotification -> {
						// Convert each entity to DTO
						ESummonNotificationDTO dto = ESummonNotificationMapper.toDTO(summonNotification);

						// Fetch ApplicationInstanceDTO using applicationNo from summonNotification
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

			log.debug("Returning list of E-SummonNotificationDTOs: {}", eSummonNotificationDTOs);
			return eSummonNotificationDTOs;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found when retrieving E-SummonNotifications: {}", e.getMessage());
			throw e; // Re-throw the exception for further handling
		} catch (Exception e) {
			log.error("Error in getAllESummonNotificationBySessionNo: ", e);
			throw new RuntimeException("Failed to retrieve E-SummonNotifications by sessionNo", e);
		}
	}

	@Transactional
	@Override
	public ESummonNotificationDTO updateESummonNotification(Long id, ESummonNotificationDTO eSummonNotificationDTO) {
		log.debug("Starting update for ESummonNotification with ID: {}", id);

		try {
			// Fetch the existing ESummonNotification
			ESummonNotification existingNotification = eSummonNotificationRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("ESummonNotification not found with id: " + id));

			// Update only the fields you want to change
			existingNotification.setStatus(eSummonNotificationDTO.getStatus());
			existingNotification.setEsummonIssuanceDateHindiEn(eSummonNotificationDTO.getEsummonIssuanceDateHindiEn());
			existingNotification.setEsummonIssuanceDate(eSummonNotificationDTO.getEsummonIssuanceDate());
			existingNotification.setEsummonIssuanceDateHindi(eSummonNotificationDTO.getEsummonIssuanceDateHindi());
			existingNotification.setTemplateContent(eSummonNotificationDTO.getTemplateContent());
			existingNotification.setTemplateContentHindi(eSummonNotificationDTO.getTemplateContentHindi());

			// Save the updated ESummonNotification
			ESummonNotification updatedNotification = eSummonNotificationRepository.save(existingNotification);

			// Prepare the response DTO
			ESummonNotificationDTO responseDTO = ESummonNotificationMapper.toDTO(updatedNotification);

			// Now, let's automatically update the ApplicationInstance
			if (updatedNotification.getStatus() != null || updatedNotification.getEsummonIssuanceDate() != null) {
			    ApplicationInstanceDTO applicationInstanceDTO = new ApplicationInstanceDTO();

			    if (updatedNotification.getStatus() != null) {
			        applicationInstanceDTO.setCurrentStatus(updatedNotification.getStatus());
			        log.debug("Setting currentStatus: {}", updatedNotification.getStatus());
			    }

			    if (updatedNotification.getEsummonIssuanceDate() != null) {
			        applicationInstanceDTO.setDocumentDate(updatedNotification.getEsummonIssuanceDate());
			        log.debug("Setting documentDate: {}", updatedNotification.getEsummonIssuanceDate());
			    }
				// Call the external service to update ApplicationInstance
				String url = applicationInstanceServiceUrl + "/updateByApplicationNo/"
						+ updatedNotification.getApplicationNo(); // Assuming field renamed
				try {
					restTemplate.put(url, applicationInstanceDTO);
					log.debug("Updated ApplicationInstance with new status for applicationNo: {}",
							updatedNotification.getApplicationNo());
				} catch (Exception e) {
					log.error("Failed to update ApplicationInstance: ", e);
					throw new RuntimeException("Failed to update ApplicationInstance", e);
				}
			}

			// Fetch the updated ApplicationInstance data
			String applicationNumber = updatedNotification.getApplicationNo(); // Get the applicationNo for the
																				// ApplicationInstance
			String applicationInstanceUrl = applicationInstanceServiceUrl + "/getApplicationNo/" + applicationNumber;

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
						log.debug("Fetched ApplicationInstance for applicationNo: {}", applicationNumber);
					}
				}
			} catch (Exception e) {
				log.error("Failed to fetch ApplicationInstance: ", e);
				// You can choose to handle this case as needed
			}
			
			 if (eSummonNotificationDTO.getTemplateContent() != null || eSummonNotificationDTO.getTemplateContentHindi() != null) {
		            Optional<ApplicationPapers> existingPapersOpt = applicationPapersRepository
		                    .findByApplicationIdAndApplicationInstanceId(eSummonNotificationDTO.getApplicationId(), responseDTO.getApplicationInstanceDTO().getId());
		            
		            if (existingPapersOpt.isPresent()) {
		                ApplicationPapers existingPapers = existingPapersOpt.get();
		                existingPapers.setTemplateContent(eSummonNotificationDTO.getTemplateContent());
		                existingPapers.setTemplateContentHindi(eSummonNotificationDTO.getTemplateContentHindi());

		                applicationPapersRepository.save(existingPapers);
		                log.debug("Updated ApplicationPapers with new template content.");
		            } else {
		                log.warn("ApplicationPapers not found for ApplicationId: {} and ApplicationInstanceId: {}",
		                         eSummonNotificationDTO.getApplicationId(), responseDTO.getApplicationInstanceDTO().getId());
		            }
		        }


			log.debug("Returning updated ESummonNotificationDTO with ApplicationInstance data: {}", responseDTO);
			return responseDTO;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found during updateESummonNotification: {}", e.getMessage());
			throw e; // Re-throw the exception for further handling
		} catch (Exception e) {
			log.error("Error in updateESummonNotification: ", e);
			throw new RuntimeException("Failed to update ESummonNotification", e);
		}
	}

	@Override
	public void deleteESummonNotification(Long id) {
		// TODO Auto-generated method stub

	}

}
