package com.dsp.agendaPapers.eSummonNotification.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonNotificationRepository;
import com.dsp.agendaPapers.eSummonNotification.service.ESummonNotificationService;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import com.dsp.agendaPapers.mapper.ESummonNotificationMapper;
import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;
import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;
import com.dsp.agendaPapers.summonNotification.entity.SummonNotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ESummonNotificationServiceImpl implements ESummonNotificationService {
	// private static int lastIncrementValue = 0;
	private static final Logger log = LoggerFactory.getLogger(ESummonNotificationServiceImpl.class);

	@Autowired
	private ESummonNotificationRepository eSummonNotificationRepository;

	@Autowired
	private RestTemplate restTemplate;

	private final String applicationInstanceServiceUrl = "http://10.247.128.16/master/application-instances";
    
	@Transactional
	@Override
	public ESummonNotificationDTO saveESummonNotification(ESummonNotificationDTO eSummonNotificationDTO) {
	    log.debug("Entering saveESummonNotification with ESummonNotificationDTO: {}", eSummonNotificationDTO);

	    try {
	        // Convert DTO to entity
	        ESummonNotification summonNotification = ESummonNotificationMapper.toEntity(eSummonNotificationDTO);
	        log.debug("Converted E-SummonNotificationDTO to entity: {}", summonNotification);

	        // Save SummonNotification entity initially to generate the ID
	        ESummonNotification savedNotification = eSummonNotificationRepository.save(summonNotification);
	        log.debug("Initial save of E-SummonNotification to generate ID: {}", savedNotification);

	        // Generate Application No using the generated ID
	        String applicationNo = generateApplicationNo(savedNotification.getLokSabhaNo(), savedNotification.getSessionNo(), savedNotification.getId());
	        savedNotification.setApplicationNo(applicationNo);
	        log.debug("Generated Application No: {}", applicationNo);

	        // Save the SummonNotification entity again with the generated Application No
	        savedNotification = eSummonNotificationRepository.save(savedNotification);
	        log.debug("Saved E-SummonNotification entity with Application No: {}", savedNotification);

	        // Log saved attributes
	        log.info("Saved E-SummonNotification attributes: ID={}, LokSabhaNo={}, SessionNo={}, SummonIssuanceDate={}, Status={}, TemplateContent={}",
	            savedNotification.getId(), savedNotification.getLokSabhaNo(), savedNotification.getSessionNo(),
	            savedNotification.getSummonIssuanceDate(), savedNotification.getStatus(), savedNotification.getTemplateContent());

	        // Create ApplicationInstanceDTO to send to the ApplicationInstance microservice
	        ApplicationInstanceDTO applicationInstanceDTO = ApplicationInstanceDTO.builder()
	                .applicationId(eSummonNotificationDTO.getApplicationId()) // From frontend
	                .applicationNo(savedNotification.getApplicationNo()) // Saved in SummonNotification
	                .currentStatus(savedNotification.getStatus()) // From frontend
	                .publishedDocumentUrl(eSummonNotificationDTO.getPublishedDocumentUrl()) // From frontend
	                .refId(savedNotification.getId()) // Use SummonNotification's ID as refId
	                .build();
	        log.debug("Created ApplicationInstanceDTO for microservice call: {}", applicationInstanceDTO);

	        // Call ApplicationInstance microservice to save the application instance data
	        ApplicationInstanceDTO savedInstanceDTO = createApplicationInstance(applicationInstanceDTO);
	        log.debug("Saved ApplicationInstanceDTO returned from microservice: {}", savedInstanceDTO);

	        // Convert saved entity back to DTO
	        ESummonNotificationDTO responseDTO = ESummonNotificationMapper.toDTO(savedNotification);
	        responseDTO.setApplicationInstanceDTO(savedInstanceDTO); // Set the ApplicationInstanceDTO
	        log.debug("Returning E-SummonNotificationDTO with ApplicationInstanceDTO: {}", responseDTO);

	        return responseDTO;
	    } catch (Exception e) {
	        log.error("Error in saveESummonNotification: ", e);
	        throw e;
	    }
	}

	private String generateApplicationNo(String lokSabhaNo, String sessionNo, Long id) {
	    // Generate application number with formatting, including the ID
	    return "ESLS" + "/" + lokSabhaNo + "/" + sessionNo + "/" + id;
	}

	private ApplicationInstanceDTO createApplicationInstance(ApplicationInstanceDTO applicationInstanceDTO) {
	    String url = applicationInstanceServiceUrl + "/create";
	    log.debug("Calling ApplicationInstance service at URL: {} with ApplicationInstanceDTO: {}", url, applicationInstanceDTO);

	    try {
	        ApplicationInstanceDTO savedInstance = restTemplate.postForObject(url, applicationInstanceDTO,
	                ApplicationInstanceDTO.class);
	        log.debug("Received response from ApplicationInstance service: {}", savedInstance);

	        // Log saved attributes of ApplicationInstanceDTO
	        log.info("Saved ApplicationInstance attributes: RefId={}, ApplicationId={}, ApplicationNo={}, CurrentStatus={}, PublishedDocumentUrl={}",
	            savedInstance.getRefId(), savedInstance.getApplicationId(), savedInstance.getApplicationNo(),
	            savedInstance.getCurrentStatus(), savedInstance.getPublishedDocumentUrl());

	        return savedInstance;
	    } catch (Exception e) {
	        log.error("Error calling ApplicationInstance service: ", e);
	        throw e;
	    }
	}


	@Override
	public List<ESummonNotificationDTO> getAllESummonNotifications() {
		log.debug("Entering getAllESummonNotifications");
		List<ESummonNotification> notifications = eSummonNotificationRepository.findAll();
		log.debug("Fetched all E-SummonNotifications: {}", notifications);

		List<ESummonNotificationDTO> notificationDTOs = new ArrayList<>();

		for (ESummonNotification notification : notifications) {
			log.debug("Processing E-SummonNotification: {}", notification);
			ESummonNotificationDTO dto = ESummonNotificationMapper.toDTO(notification);
			ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(notification.getId());
			dto.setApplicationInstanceDTO(instanceDTO);
			notificationDTOs.add(dto);
		}

		log.debug("Returning list of E-SummonNotificationDTOs: {}", notificationDTOs);
		return notificationDTOs;
	}

	private ApplicationInstanceDTO fetchApplicationInstanceData(Long refId) {
		String url = applicationInstanceServiceUrl + "/getByRefId/" + refId;
		log.debug("Fetching ApplicationInstance by refId from URL: {}", url);

		try {
			ApplicationInstanceDTO instanceDTO = restTemplate.getForObject(url, ApplicationInstanceDTO.class);
			log.debug("Fetched ApplicationInstanceDTO: {}", instanceDTO);
			return instanceDTO;
		} catch (Exception e) {
			log.error("Error fetching ApplicationInstance for refId: {}", refId, e);
			throw e;
		}
	}
	
	 private ApplicationInstanceDTO fetchApplicationInstanceData(String applicationNo) {
	        String url = applicationInstanceServiceUrl + "/getApplicationNo/" + applicationNo;
	        log.debug("Fetching ApplicationInstance by applicationNo from URL: {}", url);

	        try {
	            ApplicationInstanceDTO instanceDTO = restTemplate.getForObject(url, ApplicationInstanceDTO.class);
	            log.debug("Fetched ApplicationInstanceDTO: {}", instanceDTO);
	            return instanceDTO;
	        } catch (ResourceNotFoundException e) {
	            log.error("Resource not found for refId: {}", applicationNo);
	            throw e; // Re-throw the exception for further handling
	        } catch (Exception e) {
	            log.error("Error fetching ApplicationInstance for refId: {}", applicationNo, e);
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
		ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(notification.getId());
		dto.setApplicationInstanceDTO(instanceDTO);

		log.debug("Returning E-SummonNotificationDTO with ApplicationInstanceDTO: {}", dto);
		return dto;
	}
	
	@Override
	public List<ESummonNotificationDTO> getAllESummonNotificationBySessionNo(String sessionNo) {
	    log.debug("Entering getAllESummonNotificationBySessionNo with sessionNo: {}", sessionNo);

	    try {
	        // Fetch all ESummonNotifications with the provided sessionNo
	        List<ESummonNotification> eSummonNotifications = eSummonNotificationRepository.findBySessionNo(sessionNo);
	        
	        if (eSummonNotifications.isEmpty()) {
	            log.error("No SummonNotifications found with sessionNo: {}", sessionNo);
	            throw new ResourceNotFoundException("No SummonNotifications found with sessionNo: " + sessionNo);
	        }

	        // Convert each ESummonNotification entity to ESummonNotificationDTO
	        List<ESummonNotificationDTO> eSummonNotificationDTOs = eSummonNotifications.stream()
	                .map(summonNotification -> {
	                    // Convert each entity to DTO
	                    ESummonNotificationDTO dto = ESummonNotificationMapper.toDTO(summonNotification);
	                    
	                    // Fetch ApplicationInstanceDTO using applicationNo from summonNotification
	                    ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(summonNotification.getApplicationNo());
	                    dto.setApplicationInstanceDTO(instanceDTO);
	                    
	                    return dto;
	                })
	                .collect(Collectors.toList());

	        log.debug("Returning list of ESummonNotificationDTOs: {}", eSummonNotificationDTOs);
	        return eSummonNotificationDTOs;

	    } catch (ResourceNotFoundException e) {
	        log.error("Resource not found when retrieving SummonNotifications: {}", e.getMessage());
	        throw e; // Re-throw the exception for further handling
	    } catch (Exception e) {
	        log.error("Error in getAllESummonNotificationBySessionNo: ", e);
	        throw new RuntimeException("Failed to retrieve SummonNotifications by sessionNo", e);
	    }
	}



	@Override
	public void deleteESummonNotification(Long id) {
		log.debug("Entering deleteESummonNotification with id: {}", id);
		try {
			eSummonNotificationRepository.deleteById(id);
			log.debug("Deleted E-SummonNotification with id: {}", id);
		} catch (Exception e) {
			log.error("Error deleting E-SummonNotification with id: {}", id, e);
			throw e;
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
	        existingNotification.setTemplateContent(eSummonNotificationDTO.getTemplateContent());

	        // Save the updated ESummonNotification
	        ESummonNotification updatedNotification = eSummonNotificationRepository.save(existingNotification);
	        log.debug("Updated ESummonNotification entity: {}", updatedNotification);

	        // Now, let's automatically update the ApplicationInstance
	        if (updatedNotification.getStatus() != null) {
	            ApplicationInstanceDTO applicationInstanceDTO = new ApplicationInstanceDTO();
	            applicationInstanceDTO.setCurrentStatus(updatedNotification.getStatus()); // New status from ESummonNotification

	            // Call the external service to update ApplicationInstance by refId (which is the ID of the ESummonNotification)
	            String url = applicationInstanceServiceUrl + "/updateByRefId/" + updatedNotification.getId();
	            try {
	                restTemplate.put(url, applicationInstanceDTO);
	                log.debug("Updated ApplicationInstance with new status for refId: {}", updatedNotification.getId());
	            } catch (Exception e) {
	                log.error("Failed to update ApplicationInstance: ", e);
	                throw new RuntimeException("Failed to update ApplicationInstance", e);
	            }
	        }

	        // Convert updated entity back to DTO
	        ESummonNotificationDTO responseDTO = ESummonNotificationMapper.toDTO(updatedNotification);
	        log.debug("Returning updated ESummonNotificationDTO: {}", responseDTO);

	        return responseDTO;
	    } catch (ResourceNotFoundException e) {
	        log.error("Resource not found during updateESummonNotification: {}", e.getMessage());
	        throw e; // Re-throw the exception for further handling
	    } catch (Exception e) {
	        log.error("Error in updateESummonNotification: ", e);
	        throw new RuntimeException("Failed to update ESummonNotification", e);
	    }
	}

	
	
}
