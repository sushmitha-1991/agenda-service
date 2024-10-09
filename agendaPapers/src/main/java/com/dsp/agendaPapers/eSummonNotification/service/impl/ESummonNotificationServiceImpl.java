package com.dsp.agendaPapers.eSummonNotification.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonNotificationRepository;
import com.dsp.agendaPapers.eSummonNotification.service.ESummonNotificationService;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import com.dsp.agendaPapers.mapper.ESummonNotificationMapper;
import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;

@Service
public class ESummonNotificationServiceImpl implements ESummonNotificationService {
	
	@Autowired
	private ESummonNotificationRepository eSummonNotificationRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private final String applicationInstanceServiceUrl = "http://10.247.128.16/master/application-instances";


	@Override
	public ESummonNotificationDTO saveESummonNotification(ESummonNotificationDTO eSummonNotificationDTO) {

			// Convert DTO to entity
			ESummonNotification summonNotification = ESummonNotificationMapper.toEntity(eSummonNotificationDTO);

			// Save SummonNotification entity
			ESummonNotification savedNotification = eSummonNotificationRepository.save(summonNotification);

			// Create ApplicationInstanceDTO to send to the ApplicationInstance microservice
			ApplicationInstanceDTO applicationInstanceDTO = ApplicationInstanceDTO.builder()
					.applicationId(eSummonNotificationDTO.getApplicationInstanceDTO().getApplicationId()) // Coming from
																											// frontend
					.applicationNo(savedNotification.getApplicationNo()) // Saved in SummonNotification
					.currentStatus(eSummonNotificationDTO.getCurrentStatus()) // Coming from frontend
					.publishedDocumentUrl(eSummonNotificationDTO.getPublishedDocumentUrl()) // Coming from frontend
					.refId(savedNotification.getId()) // Use ESummonNotification's ID as refId
					.build();

			// Call ApplicationInstance microservice to save the application instance data
			ApplicationInstanceDTO savedInstanceDTO = createApplicationInstance(applicationInstanceDTO);

			// Convert saved entity back to DTO
			ESummonNotificationDTO responseDTO = ESummonNotificationMapper.toDTO(savedNotification);

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
	public List<ESummonNotificationDTO> getAllESummonNotifications() {
		List<ESummonNotification> notifications = eSummonNotificationRepository.findAll();
		List<ESummonNotificationDTO> notificationDTOs = new ArrayList<>();

		for (ESummonNotification notification : notifications) {
			ESummonNotificationDTO dto = ESummonNotificationMapper.toDTO(notification);
			ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(notification.getId());
			dto.setApplicationInstanceDTO(instanceDTO);
			notificationDTOs.add(dto);
		}

		return notificationDTOs;
	}

	private ApplicationInstanceDTO fetchApplicationInstanceData(Long notificationId) {
		// Fetch the ApplicationInstance by its ID
		String url = applicationInstanceServiceUrl + "/getByRefId/" + notificationId;
		// Use the correct URL structure
		return restTemplate.getForObject(url, ApplicationInstanceDTO.class);
	}
	

	@Override
	public ESummonNotificationDTO getESummonNotificationById(Long id) {
		ESummonNotification notification = eSummonNotificationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("e SummonNotification not found with id: " + id));

		ESummonNotificationDTO dto = ESummonNotificationMapper.toDTO(notification);

		// Fetching the application instance data
		ApplicationInstanceDTO instanceDTO = fetchApplicationInstanceData(notification.getId());
		dto.setApplicationInstanceDTO(instanceDTO);

		return dto;
	}

	

	@Override
	public void deleteESummonNotification(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ESummonNotificationDTO updateESummonNotification(Long id, ESummonNotificationDTO eSummonNotificationDTO) {
		ESummonNotification existingNotification = eSummonNotificationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("e SummonNotification not found with id: " + id));

		// Update the SummonNotification fields
		existingNotification.setLokSabhaNo(eSummonNotificationDTO.getLokSabhaNo());
		existingNotification.setSessionNo(eSummonNotificationDTO.getSessionNo());
		existingNotification.setSummonIssuanceDate(eSummonNotificationDTO.getSummonIssuanceDate());
		existingNotification.setStatus(eSummonNotificationDTO.getStatus());
		existingNotification.setTemplateContent(eSummonNotificationDTO.getTemplateContent());

		// Save the updated SummonNotification
		ESummonNotification updatedNotification = eSummonNotificationRepository.save(existingNotification);

		// Check if ApplicationInstanceDTO is not null and update ApplicationInstance
		if (eSummonNotificationDTO.getApplicationInstanceDTO() != null) {
			ApplicationInstanceDTO applicationInstanceDTO = eSummonNotificationDTO.getApplicationInstanceDTO();
			String url = applicationInstanceServiceUrl + "/updateByRefId/" + applicationInstanceDTO.getRefId();

			// Making a PUT request to update the ApplicationInstance in the other
			// microservice
			restTemplate.put(url, applicationInstanceDTO);
		}

		// Fetch the updated ApplicationInstanceDTO to include it in the response
		ApplicationInstanceDTO updatedInstanceDTO = fetchApplicationInstanceData(updatedNotification.getId());

		// Prepare the response DTO
		ESummonNotificationDTO responseDTO = ESummonNotificationMapper.toDTO(updatedNotification);
		responseDTO.setApplicationInstanceDTO(updatedInstanceDTO); // Set the updated ApplicationInstanceDTO

		return responseDTO;
	}

}
