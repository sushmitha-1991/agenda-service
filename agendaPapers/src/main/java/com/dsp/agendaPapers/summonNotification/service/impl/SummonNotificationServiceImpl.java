package com.dsp.agendaPapers.summonNotification.service.impl;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
	        String applicationNo = generateApplicationNo(summonNotificationDTO.getLokSabhaNo(), summonNotificationDTO.getSessionNo());
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

	        // Convert saved entity back to DTO
	        SummonNotificationDTO responseDTO = summonNotificationMapper.toDTO(savedNotification);
	        responseDTO.setApplicationInstanceDTO(savedInstanceDTO);
	        log.debug("Returning SummonNotificationDTO with ApplicationInstanceDTO: {}", responseDTO);

	        return responseDTO;
	    } catch (Exception e) {
	        log.error("Error in saveSummonNotification: ", e);
	        throw e;
	    }
	}

	private String generateApplicationNo(String lokSabhaNo, String sessionNo) {
	    return "Ref-SNLS" + lokSabhaNo + sessionNo + "01";
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
		} catch (Exception e) {
			log.error("Error calling ApplicationInstance service: ", e);
			throw e;
		}
	}

	



	@Override
	public void deleteSummonNotification(Long id) {
		log.debug("Entering deleteSummonNotification with ID: {}", id);

		try {
			SummonNotification existingNotification = summonNotificationRepository.findById(id).orElseThrow(() -> {
				log.error("SummonNotification not found with ID: {}", id);
				return new ResourceNotFoundException("SummonNotification not found with id: " + id);
			});

			// Delete associated ApplicationInstance
			String url = applicationInstanceServiceUrl + "/deleteByRefId/" + existingNotification.getId();
			log.debug("Calling ApplicationInstance service to delete instance at URL: {}", url);

			try {
				restTemplate.delete(url);
				log.debug("Deleted ApplicationInstance for refId: {}", existingNotification.getId());
			} catch (Exception e) {
				log.error("Error deleting ApplicationInstance with refId: {}", existingNotification.getId(), e);
				throw e;
			}

			summonNotificationRepository.delete(existingNotification);
			log.debug("Deleted SummonNotification with ID: {}", id);
		} catch (Exception e) {
			log.error("Error in deleteSummonNotification: ", e);
			throw e;
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
			throw e;
		}
	}

	@Override
	public SummonNotificationDTO getSummonNotificationById(Long id) {
		log.debug("Entering getSummonNotificationById with ID: {}", id);

		try {
			SummonNotification summonNotification = summonNotificationRepository.findById(id).orElseThrow(() -> {
				log.error("SummonNotification not found with ID: {}", id);
				return new ResourceNotFoundException("SummonNotification not found with id: " + id);
			});

			SummonNotificationDTO summonNotificationDTO = summonNotificationMapper.toDTO(summonNotification);
			ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(id);
			summonNotificationDTO.setApplicationInstanceDTO(instanceDTO);

			log.debug("Returning SummonNotificationDTO: {}", summonNotificationDTO);
			return summonNotificationDTO;
		} catch (Exception e) {
			log.error("Error in getSummonNotificationById: ", e);
			throw e;
		}
	}

	private ApplicationInstanceDTO fetchApplicationInstanceData(Long refId) {
		String url = applicationInstanceServiceUrl + "/getByRefId/" + refId;
		log.debug("Fetching ApplicationInstance by refId from URL: {}", url);

		try {
			ApplicationInstanceDTO instanceDTO = restTemplate.getForObject(url, ApplicationInstanceDTO.class);
			log.debug("Fetched ApplicationInstanceDTO from microservice: {}", instanceDTO);
			return instanceDTO;
		} catch (Exception e) {
			log.error("Error fetching ApplicationInstance by refId: {}", refId, e);
			throw e;
		}
	}

	@Transactional
	@Override
	public SummonNotificationDTO updateSummonNotification(Long id, SummonNotificationDTO summonNotificationDTO) {
	    log.debug("Starting update for SummonNotification with ID: {}", id);

	    try {
	        // Fetch the existing SummonNotification
	        SummonNotification existingNotification = summonNotificationRepository.findById(id).orElseThrow(() -> 
	            new ResourceNotFoundException("SummonNotification not found with id: " + id));

	        // Update only the fields you want to change
	        existingNotification.setLokSabhaNo(summonNotificationDTO.getLokSabhaNo());
	        existingNotification.setSessionNo(summonNotificationDTO.getSessionNo());
	        existingNotification.setSummonIssuanceDate(summonNotificationDTO.getSummonIssuanceDate());
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
	            String url = applicationInstanceServiceUrl + "/updateByRefId/" + updatedNotification.getId(); // assuming refId is available from the existingNotification
	            restTemplate.put(url, applicationInstanceDTO);
	            log.debug("Updated ApplicationInstance with new status: {}", applicationInstanceDTO.getCurrentStatus());
	        }

	        // Convert the updated notification back to DTO
	        SummonNotificationDTO responseDTO = summonNotificationMapper.toDTO(updatedNotification);
	        log.debug("Returning updated SummonNotificationDTO: {}", responseDTO);

	        return responseDTO;
	    } catch (Exception e) {
	        log.error("Error updating SummonNotification: ", e);
	        throw e;
	    }
	}




}
