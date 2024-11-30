package com.dsp.agendaPapers.prorogationnotice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.dsp.agendaPapers.applicationPapers.dto.ApplicationPapersDTO;
import com.dsp.agendaPapers.applicationPapers.dto.TemplatesDTO;
import com.dsp.agendaPapers.applicationPapers.repository.ApplicationPapersRepository;
import com.dsp.agendaPapers.applicationPapers.service.ApplicationPapersService;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import com.dsp.agendaPapers.prorogationnotice.Dto.ProrogationNoticeDTO;
import com.dsp.agendaPapers.prorogationnotice.entity.ProrogationNotice;
import com.dsp.agendaPapers.prorogationnotice.mapper.ProrogationNoticeMapper;
import com.dsp.agendaPapers.prorogationnotice.repository.ProrogationNoticeRepository;
import com.dsp.agendaPapers.prorogationnotice.service.ProrogationNoticeService;
import com.dsp.agendaPapers.response.GenericResponse;
import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;

@Service
public class ProrogationNoticeServiceImpl implements ProrogationNoticeService {
	
	@Autowired
	private ProrogationNoticeRepository prorogationNoticeRepository;
	
	@Autowired
	private ApplicationPapersService applicationPapersService;

	@Autowired
	private ProrogationNoticeMapper prorogationNoticeMapper;

	@Value("${application.instance.service.url}")
	private String applicationInstanceServiceUrl;

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
	    log.debug("Entering saveProrogationNotice with ProrogationNoticeDTO: {}", prorogationNoticeDTO);

	    try {
	        // Convert DTO to entity
	        ProrogationNotice prorogationNotice = prorogationNoticeMapper.toEntity(prorogationNoticeDTO);
	        log.debug("Converted ProrogationNoticeDTO to entity: {}", prorogationNotice);

	        // Save ProrogationNotice entity first to generate the ID
	        ProrogationNotice savedNotice = prorogationNoticeRepository.save(prorogationNotice);
	        log.debug("First save - Generated ProrogationNotice ID: {}", savedNotice.getId());

	        // Generate Application Number using the generated ID
	        String applicationNo = generateApplicationNo(
	            prorogationNoticeDTO.getLokSabhaNo(),
	            prorogationNoticeDTO.getLobSabhaSessionNo(),
	            prorogationNoticeDTO.getRajyaSabhaSessionNo(),
	            savedNotice.getId()
	        );
	        savedNotice.setApplicationNo(applicationNo);
	        log.debug("Generated Application No: {}", applicationNo);

	        // Save ProrogationNotice again with the updated applicationNo
	        savedNotice = prorogationNoticeRepository.save(savedNotice);
	        log.debug("Saved ProrogationNotice entity with updated Application No: {}", savedNotice);

	        // Create ApplicationInstanceDTO for external microservice
	        ApplicationInstanceDTO applicationInstanceDTO = ApplicationInstanceDTO.builder()
	            .applicationId(prorogationNoticeDTO.getApplicationId()) // From frontend
	            .applicationNo(savedNotice.getApplicationNo()) // Saved in ProrogationNotice
	            .currentStatus(savedNotice.getStatus()) // From ProrogationNotice's status
	            .publishedDocumentUrl(prorogationNoticeDTO.getPublishedDocumentUrl()) // Optional from frontend
	            .refId(savedNotice.getId()) // Use ProrogationNotice's ID as refId
	            .build();
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
	        ProrogationNoticeDTO responseDTO = prorogationNoticeMapper.toDto(savedNotice);
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



	private String generateApplicationNo(String lokSabhaNo, String lobSabhaSessionNo, String rajyaSabhaSessionNo, Long id) {
	    if (lokSabhaNo != null && !lokSabhaNo.isEmpty() && lobSabhaSessionNo != null && !lobSabhaSessionNo.isEmpty()) {
	        return "PNLS" + lokSabhaNo + lobSabhaSessionNo + id;
	    } else if (rajyaSabhaSessionNo != null && !rajyaSabhaSessionNo.isEmpty()) {
	        return "PNRS" + rajyaSabhaSessionNo + id;
	    } else {
	        throw new IllegalArgumentException("Insufficient data to generate application number");
	    }
	}

	private ApplicationInstanceDTO createApplicationInstance(ApplicationInstanceDTO applicationInstanceDTO) {
	    String url = applicationInstanceServiceUrl + "/create";
	    log.debug("Calling ApplicationInstance service at URL: {} with ApplicationInstanceDTO: {}", url, applicationInstanceDTO);

	    try {
	        ResponseEntity<GenericResponse<ApplicationInstanceDTO>> response = restTemplate.exchange(url,
	                HttpMethod.POST, new HttpEntity<>(applicationInstanceDTO),
	                new ParameterizedTypeReference<GenericResponse<ApplicationInstanceDTO>>() {});

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
	            log.error("Failed to fetch Template from master service. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
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
	            log.error("Response or body is null when fetching ApplicationInstance for applicationNo: {}", applicationNo);
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

}
