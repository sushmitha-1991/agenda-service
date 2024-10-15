package com.dsp.agendaPapers.eSummonNotification.service.impl;

import java.util.ArrayList;
import java.util.List;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ESummonNotificationServiceImpl implements ESummonNotificationService {

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
			log.debug("Converted ESummonNotificationDTO to entity: {}", summonNotification);

			 String applicationNo = generateApplicationNo(eSummonNotificationDTO.getLokSabhaNo(), eSummonNotificationDTO.getSessionNo());
		        summonNotification.setApplicationNo(applicationNo);
		        log.debug("Generated Application No: {}", applicationNo);
			// Save SummonNotification entity
			ESummonNotification savedNotification = eSummonNotificationRepository.save(summonNotification);
			log.debug("Saved ESummonNotification entity: {}", savedNotification);

			// Log saved attributes
			log.info("Saved ESummonNotification attributes: ID={}, LokSabhaNo={}, SessionNo={}, SummonIssuanceDate={}, Status={}, TemplateContent={}",
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
			log.debug("Returning ESummonNotificationDTO with ApplicationInstanceDTO: {}", responseDTO);

			return responseDTO;
		} catch (Exception e) {
			log.error("Error in saveESummonNotification: ", e);
			throw e;
		}
	}
	
	private String generateApplicationNo(String lokSabhaNo, String sessionNo) {
	    return "Ref-SNLS" + lokSabhaNo + sessionNo + "01";
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
		log.debug("Fetched all ESummonNotifications: {}", notifications);

		List<ESummonNotificationDTO> notificationDTOs = new ArrayList<>();

		for (ESummonNotification notification : notifications) {
			log.debug("Processing ESummonNotification: {}", notification);
			ESummonNotificationDTO dto = ESummonNotificationMapper.toDTO(notification);
			ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(notification.getId());
			dto.setApplicationInstanceDTO(instanceDTO);
			notificationDTOs.add(dto);
		}

		log.debug("Returning list of ESummonNotificationDTOs: {}", notificationDTOs);
		return notificationDTOs;
	}

	private ApplicationInstanceDTO fetchApplicationInstanceData(Long notificationId) {
		String url = applicationInstanceServiceUrl + "/getByRefId/" + notificationId;
		log.debug("Fetching ApplicationInstance by refId from URL: {}", url);

		try {
			ApplicationInstanceDTO instanceDTO = restTemplate.getForObject(url, ApplicationInstanceDTO.class);
			log.debug("Fetched ApplicationInstanceDTO: {}", instanceDTO);
			return instanceDTO;
		} catch (Exception e) {
			log.error("Error fetching ApplicationInstance for refId: {}", notificationId, e);
			throw e;
		}
	}

	@Override
	public ESummonNotificationDTO getESummonNotificationById(Long id) {
		log.debug("Entering getESummonNotificationById with id: {}", id);

		ESummonNotification notification = eSummonNotificationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("e SummonNotification not found with id: " + id));
		log.debug("Fetched ESummonNotification by id: {}", notification);

		ESummonNotificationDTO dto = ESummonNotificationMapper.toDTO(notification);
		ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(notification.getId());
		dto.setApplicationInstanceDTO(instanceDTO);

		log.debug("Returning ESummonNotificationDTO with ApplicationInstanceDTO: {}", dto);
		return dto;
	}

	@Override
	public void deleteESummonNotification(Long id) {
		log.debug("Entering deleteESummonNotification with id: {}", id);
		try {
			eSummonNotificationRepository.deleteById(id);
			log.debug("Deleted ESummonNotification with id: {}", id);
		} catch (Exception e) {
			log.error("Error deleting ESummonNotification with id: {}", id, e);
			throw e;
		}
	}

	@Override
	public ESummonNotificationDTO updateESummonNotification(Long id, ESummonNotificationDTO eSummonNotificationDTO) {
		log.debug("Entering updateESummonNotification with id: {} and ESummonNotificationDTO: {}", id, eSummonNotificationDTO);

		ESummonNotification existingNotification = eSummonNotificationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("e SummonNotification not found with id: " + id));
		log.debug("Fetched existing ESummonNotification for update: {}", existingNotification);

		// Update the ESummonNotification fields
		existingNotification.setLokSabhaNo(eSummonNotificationDTO.getLokSabhaNo());
		existingNotification.setSessionNo(eSummonNotificationDTO.getSessionNo());
		existingNotification.setSummonIssuanceDate(eSummonNotificationDTO.getSummonIssuanceDate());
		existingNotification.setStatus(eSummonNotificationDTO.getStatus());
		existingNotification.setTemplateContent(eSummonNotificationDTO.getTemplateContent());
		log.debug("Updated ESummonNotification fields: {}", existingNotification);

		// Save the updated SummonNotification
		ESummonNotification updatedNotification = eSummonNotificationRepository.save(existingNotification);
		log.debug("Saved updated ESummonNotification: {}", updatedNotification);

		// Log saved attributes
		log.info("Updated ESummonNotification attributes: ID={}, LokSabhaNo={}, SessionNo={}, SummonIssuanceDate={}, Status={}, TemplateContent={}",
			updatedNotification.getId(), updatedNotification.getLokSabhaNo(), updatedNotification.getSessionNo(),
			updatedNotification.getSummonIssuanceDate(), updatedNotification.getStatus(), updatedNotification.getTemplateContent());

		// Check if ApplicationInstanceDTO is not null and update ApplicationInstance
		if (eSummonNotificationDTO.getApplicationInstanceDTO() != null) {
			ApplicationInstanceDTO applicationInstanceDTO = eSummonNotificationDTO.getApplicationInstanceDTO();
			String url = applicationInstanceServiceUrl + "/updateByRefId/" + updatedNotification.getId();
			log.debug("Updating ApplicationInstance at URL: {} with ApplicationInstanceDTO: {}", url, applicationInstanceDTO);

			try {
				restTemplate.put(url, applicationInstanceDTO);
				log.debug("Updated ApplicationInstance successfully");
			} catch (Exception e) {
				log.error("Error updating ApplicationInstance for refId: {}", updatedNotification.getId(), e);
				throw e;
			}
		}

		// Convert updated entity back to DTO
		ESummonNotificationDTO responseDTO = ESummonNotificationMapper.toDTO(updatedNotification);
		responseDTO.setApplicationInstanceDTO(eSummonNotificationDTO.getApplicationInstanceDTO());

		log.debug("Returning updated ESummonNotificationDTO: {}", responseDTO);
		return responseDTO;
	}
}
