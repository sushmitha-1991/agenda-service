package com.dsp.agendaPapers.summonNotification.service.impl;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.dsp.agendaPapers.exception.ExternalServiceException;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import com.dsp.agendaPapers.mapper.SummonNotificationMapper;
import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;
import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;
import com.dsp.agendaPapers.summonNotification.entity.SummonNotification;
import com.dsp.agendaPapers.summonNotification.repository.SummonNotificationRepository;
import com.dsp.agendaPapers.summonNotification.service.SummonNotificationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SummonNotificationServiceImpl implements SummonNotificationService {
	
	 private static int lastIncrementValue = 0;

    private static final Logger log = LoggerFactory.getLogger(SummonNotificationServiceImpl.class);

    @Autowired
    private SummonNotificationRepository summonNotificationRepository;

    @Autowired
    private SummonNotificationMapper summonNotificationMapper;

    private final String applicationInstanceServiceUrl = "http://10.247.128.16/master/application-instances";

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    @Override
    public SummonNotificationDTO saveSummonNotification(SummonNotificationDTO summonNotificationDTO) {
        log.debug("Entering saveSummonNotification with SummonNotificationDTO: {}", summonNotificationDTO);

        try {
            // Convert DTO to entity
            SummonNotification summonNotification = summonNotificationMapper.toEntity(summonNotificationDTO);
            log.debug("Converted SummonNotificationDTO to entity: {}", summonNotification);

            // Generate Application Number before saving
            String applicationNo = generateApplicationNo(summonNotificationDTO.getLokSabhaNo(),
                    summonNotificationDTO.getSessionNo());
            summonNotification.setApplicationNo(applicationNo);
            log.debug("Generated Application No: {}", applicationNo);

            // Save SummonNotification entity
            SummonNotification savedNotification = summonNotificationRepository.save(summonNotification);
            log.debug("Saved SummonNotification entity: {}", savedNotification);

            // Create ApplicationInstanceDTO for external microservice
            ApplicationInstanceDTO applicationInstanceDTO = ApplicationInstanceDTO.builder()
            		
                    .applicationId(summonNotificationDTO.getApplicationId()) // From frontend
                    .applicationNo(savedNotification.getApplicationNo()) // Saved in SummonNotification
                    .currentStatus(savedNotification.getStatus()) // From frontend
                    .publishedDocumentUrl(summonNotificationDTO.getPublishedDocumentUrl()) // From frontend
                    .refId(savedNotification.getId()) // Use SummonNotification's ID as refId
                    .build();
            log.debug("Created ApplicationInstanceDTO for microservice call: {}", applicationInstanceDTO);

            // Call ApplicationInstance microservice to save the application instance data
            ApplicationInstanceDTO savedInstanceDTO = createApplicationInstance(applicationInstanceDTO);
            log.debug("Saved ApplicationInstanceDTO returned from microservice: {}", savedInstanceDTO);

            Long applicationInstanceId = savedInstanceDTO.getId();
            log.debug("ApplicationInstance ID retrieved: {}", applicationInstanceId);

            // Convert saved entity back to DTO
            SummonNotificationDTO responseDTO = summonNotificationMapper.toDTO(savedNotification);
            responseDTO.setApplicationInstanceDTO(savedInstanceDTO);
            log.debug("Returning SummonNotificationDTO with ApplicationInstanceDTO: {}", responseDTO);

            return responseDTO;
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found during saveSummonNotification: {}", e.getMessage());
            throw e; // Re-throw the exception for further handling
        } catch (Exception e) {
            log.error("Error in saveSummonNotification: ", e);
            throw new RuntimeException("Failed to save SummonNotification", e);
        }
    }

    private String generateApplicationNo(String lokSabhaNo, String sessionNo) {
        // Increment the last used value
        lastIncrementValue++;

        // Generate application number with formatting
        return "Ref-SNLS" + lokSabhaNo + sessionNo + String.format("%02d", lastIncrementValue);
    }

    private ApplicationInstanceDTO createApplicationInstance(ApplicationInstanceDTO applicationInstanceDTO) {
        String url = applicationInstanceServiceUrl + "/create";
        log.debug("Calling ApplicationInstance service at URL: {} with ApplicationInstanceDTO: {}", url,
                applicationInstanceDTO);

        try {
            ApplicationInstanceDTO savedInstance = restTemplate.postForObject(url, applicationInstanceDTO,
                    ApplicationInstanceDTO.class);
            log.debug("Received response from ApplicationInstance service: {}", savedInstance);
            return savedInstance;
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found when calling ApplicationInstance service: {}", e.getMessage());
            throw e; // Re-throw the exception for further handling
        } catch (Exception e) {
            log.error("Error calling ApplicationInstance service: ", e);
            throw new RuntimeException("Failed to create ApplicationInstance", e);
        }
    }

    @Override
    public List<SummonNotificationDTO> getAllSummonNotifications() {
        log.debug("Entering getAllSummonNotifications");

        try {
            List<SummonNotification> summonNotifications = summonNotificationRepository.findAll();
            log.debug("Fetched all SummonNotifications from database: {}", summonNotifications);

            List<SummonNotificationDTO> responseList = new ArrayList<>();
            for (SummonNotification notification : summonNotifications) {
                SummonNotificationDTO dto = summonNotificationMapper.toDTO(notification);
                ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(notification.getId());
                dto.setApplicationInstanceDTO(instanceDTO);
                responseList.add(dto);
            }

            log.debug("Returning all SummonNotificationDTOs with ApplicationInstanceDTOs: {}", responseList);
            return responseList;
        } catch (Exception e) {
            log.error("Error in getAllSummonNotifications: ", e);
            throw new RuntimeException("Failed to retrieve SummonNotifications", e);
        }
    }

    @Override
    public SummonNotificationDTO getSummonNotificationById(Long id) {
        log.debug("Entering getSummonNotificationById with ID: {}", id);

        try {
            // Find the SummonNotification entity by ID or throw an exception if not found
            SummonNotification summonNotification = summonNotificationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("SummonNotification not found with ID: {}", id);
                    return new ResourceNotFoundException("SummonNotification not found with id: " + id);
                });

            // Convert the SummonNotification entity to a DTO
            SummonNotificationDTO summonNotificationDTO = summonNotificationMapper.toDTO(summonNotification);

            // Fetch ApplicationInstanceDTO using the applicationNo from SummonNotification
            ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(summonNotification.getApplicationNo());
            summonNotificationDTO.setApplicationInstanceDTO(instanceDTO);

            log.debug("Returning SummonNotificationDTO: {}", summonNotificationDTO);
            return summonNotificationDTO;

        } catch (ResourceNotFoundException e) {
            log.error("Resource not found when retrieving SummonNotification: {}", e.getMessage());
            throw e; // Re-throw the exception for further handling
        } catch (Exception e) {
            log.error("Error in getSummonNotificationById: ", e);
            throw new RuntimeException("Failed to retrieve SummonNotification by ID", e);
        }
    }


    private ApplicationInstanceDTO fetchApplicationInstanceData(Long refId) {
        String url = applicationInstanceServiceUrl + "/getByRefId/" + refId;
        log.debug("Fetching ApplicationInstance by applicationNo from URL: {}", url);

        try {
            ApplicationInstanceDTO instanceDTO = restTemplate.getForObject(url, ApplicationInstanceDTO.class);
            log.debug("Fetched ApplicationInstanceDTO: {}", instanceDTO);
            return instanceDTO;
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found for refId: {}", refId);
            throw e; // Re-throw the exception for further handling
        } catch (Exception e) {
            log.error("Error fetching ApplicationInstance for refId: {}", refId, e);
            throw new RuntimeException("Failed to fetch ApplicationInstance data", e);
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

    @Transactional
    @Override
    public SummonNotificationDTO updateSummonNotification(Long id, SummonNotificationDTO summonNotificationDTO) {
        log.debug("Starting update for SummonNotification with ID: {}", id);

        try {
            // Fetch the existing SummonNotification
            SummonNotification existingNotification = summonNotificationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("SummonNotification not found with id: " + id));

            // Update only the fields you want to change
         //   existingNotification.setLokSabhaNo(summonNotificationDTO.getLokSabhaNo());
           // existingNotification.setSessionNo(summonNotificationDTO.getSessionNo());
        //    existingNotification.setSummonIssuanceDate(summonNotificationDTO.getSummonIssuanceDate());
            existingNotification.setNotificationNo(summonNotificationDTO.getNotificationNo());
            existingNotification.setStatus(summonNotificationDTO.getStatus());
            existingNotification.setSummonStartAt(summonNotificationDTO.getSummonStartAt());
            existingNotification.setTemplateContent(summonNotificationDTO.getTemplateContent());

            // Save the updated SummonNotification
            SummonNotification updatedNotification = summonNotificationRepository.save(existingNotification);

            // Now, let's automatically update the ApplicationInstance
            if (updatedNotification.getStatus() != null) {
                ApplicationInstanceDTO applicationInstanceDTO = new ApplicationInstanceDTO();
                applicationInstanceDTO.setCurrentStatus(updatedNotification.getStatus()); // New status from SummonNotification

                // Call the external service to update ApplicationInstance
                String url = applicationInstanceServiceUrl + "/updateByRefId/" + updatedNotification.getId();
                try {
                    restTemplate.put(url, applicationInstanceDTO);
                    log.debug("Updated ApplicationInstance with new status for refId: {}", updatedNotification.getId());
                } catch (Exception e) {
                    log.error("Failed to update ApplicationInstance: ", e);
                    throw new RuntimeException("Failed to update ApplicationInstance", e);
                }
            }

            SummonNotificationDTO responseDTO = summonNotificationMapper.toDTO(updatedNotification);
            log.debug("Returning updated SummonNotificationDTO: {}", responseDTO);
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
	                    ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(summonNotification.getApplicationNo());
	                    dto.setApplicationInstanceDTO(instanceDTO);
	                    return dto;
	                })
	                .collect(Collectors.toList());

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

	    try {
	        // Fetch the SummonNotification or throw a ResourceNotFoundException
	        SummonNotification summonNotification = summonNotificationRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("SummonNotification not found with id: " + id));

	        // First, try to delete the associated ApplicationInstance by calling the external microservice
	        String url = applicationInstanceServiceUrl + "/deleteByApplicationNo/" + summonNotification.getApplicationNo();
	        try {
	            restTemplate.delete(url);
	            log.debug("Deleted ApplicationInstance associated with refId: {}", summonNotification.getApplicationNo());
	        } catch (Exception e) {
	            log.error("Failed to delete ApplicationInstance: ", e);
	            // Throw an exception here, preventing the SummonNotification deletion from happening
	            throw new ExternalServiceException("Failed to delete ApplicationInstance", e);
	        }

	        // If ApplicationInstance deletion was successful, proceed to delete SummonNotification
	        summonNotificationRepository.delete(summonNotification);
	        log.debug("Deleted SummonNotification with ID: {}", id);

	    } catch (ResourceNotFoundException e) {
	        log.error("Resource not found during deleteSummonNotification: {}", e.getMessage());
	        throw e; // Re-throw the exception for further handling by the caller
	    } catch (ExternalServiceException e) {
	        // Handle the custom exception for ApplicationInstance deletion failure
	        log.error("Error during ApplicationInstance deletion: {}", e.getMessage());
	        throw e;
	    } catch (Exception e) {
	        log.error("Error in deleteSummonNotification: ", e);
	        throw new RuntimeException("Failed to delete SummonNotification", e);
	    }
	}

}
