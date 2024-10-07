package com.dsp.agendaPapers.summonNotification.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import com.dsp.agendaPapers.mapper.SummonNotificationMapper;
import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;
import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;
import com.dsp.agendaPapers.summonNotification.entity.SummonNotification;
import com.dsp.agendaPapers.summonNotification.repository.SummonNotificationRepository;
import com.dsp.agendaPapers.summonNotification.service.SummonNotificationService;

@Service
public class SummonNotificationServiceImpl implements SummonNotificationService {

	@Autowired
	private SummonNotificationRepository summonNotificationRepository;

	@Autowired
	private SummonNotificationMapper summonNotificationMapper;

	private final String applicationInstanceServiceUrl = "http://localhost:8084/api/application-instances";

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public SummonNotificationDTO saveSummonNotification(SummonNotificationDTO summonNotificationDTO) {
		// Convert DTO to entity
		SummonNotification summonNotification = summonNotificationMapper.toEntity(summonNotificationDTO);

		// Save SummonNotification entity
		SummonNotification savedNotification = summonNotificationRepository.save(summonNotification);

		// Create ApplicationInstanceDTO to send to the ApplicationInstance microservice
		ApplicationInstanceDTO applicationInstanceDTO = ApplicationInstanceDTO.builder()
				.applicationNo(savedNotification.getApplicationNo()) // Assuming this is set in the DTO
				.currentStatus(summonNotificationDTO.getCurrentStatus()) // Getting from DTO
				.applicationId(savedNotification.getId()) // Use the saved notification ID
				.publishedDocumentUrl(summonNotificationDTO.getPublishedDocumentUrl()) // Getting from DTO
				.build();

		// Call ApplicationInstance microservice to save the application instance data
		ApplicationInstanceDTO savedInstanceDTO = createApplicationInstance(applicationInstanceDTO);

		// Convert saved entity back to DTO
		SummonNotificationDTO responseDTO = summonNotificationMapper.toDTO(savedNotification);

		// Ensure ApplicationInstanceDTO is included in the response
		responseDTO.setApplicationInstanceDTO(savedInstanceDTO); // Set the ApplicationInstanceDTO

		return responseDTO;
	}

	private ApplicationInstanceDTO createApplicationInstance(ApplicationInstanceDTO applicationInstanceDTO) {
		String url = applicationInstanceServiceUrl + "/create";
		return restTemplate.postForObject(url, applicationInstanceDTO,
				ApplicationInstanceDTO.class);
	}

	 @Override
	    public List<SummonNotificationDTO> getAllSummonNotifications() {
	        List<SummonNotification> notifications = summonNotificationRepository.findAll();
	        List<SummonNotificationDTO> notificationDTOs = new ArrayList<>();

	        for (SummonNotification notification : notifications) {
	            SummonNotificationDTO dto = summonNotificationMapper.toDTO(notification);
	            ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(notification.getId());
	            dto.setApplicationInstanceDTO(instanceDTO);
	            notificationDTOs.add(dto);
	        }

	        return notificationDTOs;
	    }

	    private ApplicationInstanceDTO fetchApplicationInstanceData(Long notificationId) {
	        // Fetch the ApplicationInstance by its ID
	        String url = applicationInstanceServiceUrl + "/getByApplicationId/" + notificationId; // Use the correct URL structure
	        return restTemplate.getForObject(url, ApplicationInstanceDTO.class);
	    }

	    @Override
	    public SummonNotificationDTO getSummonNotificationById(Long id) {
	        SummonNotification notification = summonNotificationRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("SummonNotification not found with id: " + id));
	        
	        SummonNotificationDTO dto = summonNotificationMapper.toDTO(notification);
	        
	        // Fetching the application instance data
	        ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(notification.getId());
	        dto.setApplicationInstanceDTO(instanceDTO);
	        
	        return dto;
	    }
	    
	    @Override
	    public SummonNotificationDTO updateSummonNotification(Long id, SummonNotificationDTO summonNotificationDTO) {
	        // Fetch the existing summonNotification
	        SummonNotification existingNotification = summonNotificationRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("SummonNotification not found with id: " + id));
	        
	        // Update the SummonNotification fields
	        existingNotification.setLokSabhaNo(summonNotificationDTO.getLokSabhaNo());
	        existingNotification.setSessionNo(summonNotificationDTO.getSessionNo());
	        existingNotification.setSummonIssuanceDate(summonNotificationDTO.getSummonIssuanceDate());
	        existingNotification.setNotificationNo(summonNotificationDTO.getNotificationNo());
	        existingNotification.setStatus(summonNotificationDTO.getStatus());
	        existingNotification.setTemplateContent(summonNotificationDTO.getTemplateContent());
	        
	        // Save the updated SummonNotification
	        SummonNotification updatedNotification = summonNotificationRepository.save(existingNotification);

	        // Check if ApplicationInstanceDTO is not null and update ApplicationInstance
	        if (summonNotificationDTO.getApplicationInstanceDTO() != null) {
	            ApplicationInstanceDTO applicationInstanceDTO = summonNotificationDTO.getApplicationInstanceDTO();
	            String url = applicationInstanceServiceUrl + "/updateByApplicationId/" + applicationInstanceDTO.getApplicationId();

	            // Making a PUT request to update the ApplicationInstance in the other microservice
	            restTemplate.put(url, applicationInstanceDTO);
	        }

	        // Fetch the updated ApplicationInstanceDTO to include it in the response
	        ApplicationInstanceDTO updatedInstanceDTO = fetchApplicationInstanceData(updatedNotification.getId());

	        // Prepare the response DTO
	        SummonNotificationDTO responseDTO = summonNotificationMapper.toDTO(updatedNotification);
	        responseDTO.setApplicationInstanceDTO(updatedInstanceDTO); // Set the updated ApplicationInstanceDTO
	        
	        return responseDTO;
	    }

	    @Override
	    public void deleteSummonNotification(Long id) {
	        // Fetch the existing summonNotification
	        SummonNotification existingNotification = summonNotificationRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("SummonNotification not found with id: " + id));

	        // Delete the associated ApplicationInstance by applicationId
	        String url = applicationInstanceServiceUrl + "/deleteByApplicationId/" + existingNotification.getId(); // Use the correct field for the application ID
	        restTemplate.delete(url);

	        // Now delete the SummonNotification
	        summonNotificationRepository.delete(existingNotification);
	    }


}
