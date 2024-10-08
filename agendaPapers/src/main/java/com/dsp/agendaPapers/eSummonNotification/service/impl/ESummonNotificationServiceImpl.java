package com.dsp.agendaPapers.eSummonNotification.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonNotificationRepository;
import com.dsp.agendaPapers.eSummonNotification.service.ESummonNotificationService;
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
					.applicationNo(savedNotification.getApplicationNo()) // Assuming this is set in the DTO
					.currentStatus(eSummonNotificationDTO.getCurrentStatus()) // Getting from DTO
					.applicationId(savedNotification.getId()) // Use the saved notification ID
					.publishedDocumentUrl(eSummonNotificationDTO.getPublishedDocumentUrl()) // Getting from DTO
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ESummonNotificationDTO getESummonNotificationById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public void deleteESummonNotification(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ESummonNotificationDTO updateESummonNotification(Long id, ESummonNotificationDTO eSummonNotificationDTO) {
		// TODO Auto-generated method stub
		return null;
	}

}
