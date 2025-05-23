package com.dsp.agendaPapers.eSummonNotification.service.impl;

import com.dsp.agendaPapers.applicationPapers.dto.ApplicationPapersDTO;
import com.dsp.agendaPapers.applicationPapers.dto.TemplatesDTO;
import com.dsp.agendaPapers.applicationPapers.entity.ApplicationPapers;
import com.dsp.agendaPapers.applicationPapers.repository.ApplicationPapersRepository;
import com.dsp.agendaPapers.applicationPapers.service.ApplicationPapersService;
import com.dsp.agendaPapers.dto.UserDetailsDTO;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonHistory;
import com.dsp.agendaPapers.eSummonNotification.entity.PersonalDetails;
import com.dsp.agendaPapers.eSummonNotification.event.EventProducerService;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonHistoryRepository;
import com.dsp.agendaPapers.eSummonNotification.repository.PersonalDetailsRepository;
import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.entityrecord.ESummonMembers;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonMembersRepository;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonNotificationRepository;
import com.dsp.agendaPapers.eSummonNotification.service.ESummonNotificationService;
import com.dsp.agendaPapers.entity.MastersLokSabhaSession;
import com.dsp.agendaPapers.entity.MastersRajyaSabha;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import com.dsp.agendaPapers.mapper.ESummonNotificationMapper;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.InputInstanceDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberRequest;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberResponse;
import com.dsp.agendaPapers.provisionalcalendarfinal.entity.InputInstance;
import com.dsp.agendaPapers.provisionalcalendarfinal.feignclient.BearerClient;
import com.dsp.agendaPapers.provisionalcalendarfinal.mapper.InputInstanceMapper;
import com.dsp.agendaPapers.provisionalcalendarfinal.repository.InputInstanceRepository;
import com.dsp.agendaPapers.repository.MasterLSSRepository;
import com.dsp.agendaPapers.repository.MastersRSSRepository;
import com.dsp.agendaPapers.response.GenericResponse;
import com.dsp.agendaPapers.summonNotification.dto.ApplicationInstanceDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Lazy;

@Service
public class ESummonNotificationServiceImpl implements ESummonNotificationService {

    private static final Logger log = LoggerFactory.getLogger(ESummonNotificationServiceImpl.class);

    @Autowired
    private ESummonNotificationRepository eSummonNotificationRepository;

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Autowired
    private MastersRSSRepository mastersRSSRepository;

    @Autowired
    private MasterLSSRepository masterLSSRepository;
    @Autowired
    private ApplicationPapersService applicationPapersService;

    @Value("${template.service.url}") // URL to validate template ID
    private String templateServiceUrl;

    @Value("${application.instance.service.url}")
    private String applicationInstanceServiceUrl;

    @Autowired
    private BearerClient bearerClient;
    @Autowired
    private ApplicationPapersRepository applicationPapersRepository;

    @Autowired
    private InputInstanceRepository inputInstanceRepository;

    @Autowired
    private PersonalDetailsRepository personalDetailsRepository;

    @Autowired
    private ESummonMembersRepository eSummonMembersRepository;

    @Autowired
    ESummonHistoryRepository eSummonHistoryRepository;

    @Autowired
    EventProducerService eventProducerService;

    @Autowired
    private HttpServletRequest httpRequest;

//working code with kafka event
    //Updated sendESummon Method with Kafka Event:
//    @Override
//    public List<PersonalDetails> sendESummon(Long eSummonId, List<Long> memberIds) {
//        Optional<ESummonNotification> optionalNotification = eSummonNotificationRepository.findById(eSummonId);
//        if (!optionalNotification.isPresent()) {
//            throw new RuntimeException("eSummonNotification not found for id: " + eSummonId);
//        }
//
//        ESummonNotification eSummon = optionalNotification.get();
//
//        // Retrieve member details
//        List<PersonalDetails> members = personalDetailsRepository.findAllById(memberIds);
//
//        // Save to e_summon_members table
//        List<ESummonMembers> summonMembersList = memberIds.stream()
//                .map(memberId -> new ESummonMembers(eSummonId, memberId))
//                .collect(Collectors.toList());
//        eSummonMembersRepository.saveAll(summonMembersList);
//
//        // Save to e_summon_history table
//        List<ESummonHistory> historyList = members.stream()
//                .map(member -> ESummonHistory.builder()
//                        .eSummon(eSummon)
//                        .member(member)
//                        .documentUuid(null) // Now allowed to be null
//                        .mailSentAt(LocalDateTime.now())
//                        .mailStatus("PENDING")
//                        .retryCount(0)
//                        .emailId(member.getEmailId()) // fixed method name
//                        .build()
//                ).collect(Collectors.toList());
//        eSummonHistoryRepository.saveAll(historyList);
//
//        // ✅ Kafka event trigger
//        eventProducerService.generateEvent(String.valueOf(eSummonId));
//        return members;
//    }


    @Transactional
    @Override
    public ESummonNotificationDTO saveESummonNotification(ESummonNotificationDTO eSummonNotificationDTO) {
        log.debug("Entering saveESummonNotification with ESummonNotificationDTO: {}", eSummonNotificationDTO);

        Long houseId = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsDTO) {
            houseId = ((UserDetailsDTO) principal).getHouseId();
        }

        SerialNumberRequest serialNumberRequest = new SerialNumberRequest();
        serialNumberRequest.setAppId(eSummonNotificationDTO.getApplicationId());
        if (houseId == 1) {
            serialNumberRequest.setSessionId(mastersRSSRepository.findLatestByCreatedBy());
        } else if (houseId == 2) {
            serialNumberRequest.setSessionId(masterLSSRepository.findLatestByCreatedBy());
        }
        //changes are need to be done according to LS and RS
        System.out.println("##############  Application no generation");
        // SerialNumberResponse applicationNo=generateApplicationNo(serialNumberRequest);

        try {
            // Convert DTO to entity
            ESummonNotification summonNotification = ESummonNotificationMapper.toEntity(eSummonNotificationDTO);
            if (StringUtils.isBlank(summonNotification.getApplicationNo())) {
                SerialNumberResponse applicationNo = bearerClient.getResource(serialNumberRequest);
                System.out.println(applicationNo);
                summonNotification.setApplicationNo(applicationNo.getId());
            }
            log.debug("Converted E-SummonNotificationDTO to entity: {}", summonNotification);
            System.out.println("@@@@@@@@@@@@@" + summonNotification);

            if (houseId == 1) {
                MastersRajyaSabha session = mastersRSSRepository.findById(mastersRSSRepository.findLatestByCreatedBy())
                        .get();
                summonNotification.setRajyasabhaSessionNo(session.getSessionNumber());
                summonNotification.setLokSabhaNo("");
                summonNotification.setSessionNo("");

            } else if (houseId == 2) {
                MastersLokSabhaSession session = masterLSSRepository
                        .findById(masterLSSRepository.findLatestByCreatedBy())
                        .get();
                summonNotification.setSessionNo(session.getSessionNumber());
                summonNotification.setLokSabhaNo(session.getLokSabha().getLokSabhaNumber());
                summonNotification.setRajyasabhaSessionNo("");

            }
            // Save SummonNotification entity initially to generate the ID
            ESummonNotification savedNotification = eSummonNotificationRepository.save(summonNotification);
            log.debug("Initial save of E-SummonNotification to generate ID: {}", savedNotification);
            savedNotification = eSummonNotificationRepository.save(savedNotification);

            // Save the SummonNotification entity again with the generated Application No
            // savedNotification = eSummonNotificationRepository.save(savedNotification);
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
                    .refId(savedNotification.getId())
                    // changes on 08-01-2025
                    .lokSabhaSession(summonNotification.getLokSabhaNo())
                    .lokSabhaSessionNumber(summonNotification.getSessionNo())
                    .rajyaSabhaSession(summonNotification.getRajyasabhaSessionNo())
                    // ends here
                    .htmlContentEnglish(summonNotification.getTemplateContent())
                    .htmlContentHindi(summonNotification.getTemplateContentHindi())
                    // Use SummonNotification's ID as refId
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

            InputInstance inputInstanceEntity = InputInstance.builder()
                    // .id(savedNotification.getId())
                    .applicationId(eSummonNotificationDTO.getApplicationId()) // From frontend
                    .applicationNo(savedNotification.getApplicationNo()) // Saved in SummonNotification
                    .currentStatus(savedNotification.getStatus()) // From frontend
                    .documentDate(savedNotification.getEsummonIssuanceDate())
                    .publishedDocumentUrl(eSummonNotificationDTO.getPublishedDocumentUrl())
                    .lokSabhaSession(summonNotification.getLokSabhaNo())
                    .lokSabhaSessionNumber(summonNotification.getSessionNo())
                    .rajyaSabhaSession(summonNotification.getRajyasabhaSessionNo())
                    .htmlContentEnglish(summonNotification.getTemplateContent())
                    .htmlContentHindi(summonNotification.getTemplateContentHindi())
                    // From frontend
                    .refId(savedNotification.getId()) // Use SummonNotification's ID as refId
                    .build();
            System.out.println("&&&&&&&&&&&&" + inputInstanceEntity);
            log.debug("Created InputInstance entity: {}", inputInstanceEntity);

            inputInstanceEntity = inputInstanceRepository.save(inputInstanceEntity);
            log.debug("Saved InputInstance entity: {}", inputInstanceEntity);
            // Convert saved InputInstance entity to DTO
            InputInstanceDTO inputInstanceDTO = InputInstanceMapper.toDTO(inputInstanceEntity);

            // Add InputInstanceDTO to ESummonNotificationDTO
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
            responseDTO.setInputInstanceDTO(inputInstanceDTO);
            log.debug("Added InputInstanceDTO to response: {}", inputInstanceDTO);
            responseDTO.setTemplateId(eSummonNotificationDTO.getTemplateId());
            responseDTO.setTemplateHindiId(eSummonNotificationDTO.getTemplateHindiId());

            log.debug("Returning E-SummonNotificationDTO with ApplicationInstanceDTO and Template ID: {}", responseDTO);

            if (!StringUtils.isBlank(eSummonNotificationDTO.getApplicationNo())) {
                // Get the Draft ID from draft table using appNo
                String res = bearerClient.deleteDraftEntry(eSummonNotificationDTO.getApplicationNo());
                System.out.println("res:::::::" + res);
                // /delete-save-as-draft
            }
            return responseDTO;
        } catch (Exception e) {
            log.error("Error in saveESummonNotification: ", e);
            e.printStackTrace();
            throw e;
        }
    }

    private ApplicationInstanceDTO createApplicationInstance(ApplicationInstanceDTO applicationInstanceDTO) {
        String url = applicationInstanceServiceUrl + "/create";
        System.out.println("### url - " + url);
        System.out.println("### applicationInstanceDTO - " + applicationInstanceDTO);
        log.debug("Calling ApplicationInstance service at URL: {} with ApplicationInstanceDTO: {}", url,
                applicationInstanceDTO);

        try {
            ResponseEntity<GenericResponse<ApplicationInstanceDTO>> response = restTemplate.exchange(url,
                    HttpMethod.POST, new HttpEntity<>(applicationInstanceDTO),
                    new ParameterizedTypeReference<GenericResponse<ApplicationInstanceDTO>>() {
                    });
            System.out.println("### response - " + response);

            if (response == null || response.getBody() == null || response.getBody().getData() == null) {
                log.error("Response or body is null from ApplicationInstance service");
                throw new RuntimeException("Failed to create ApplicationInstance, response is null");
            }

            ApplicationInstanceDTO savedInstance = response.getBody().getData();
            log.debug("Received ApplicationInstanceDTO from ApplicationInstance service: {}", savedInstance);
            return savedInstance;

        } catch (ResourceNotFoundException e) {
            log.error("Resource not found when calling ApplicationInstance service: {}", e.getMessage());
            throw e;
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
        // changes of Sushmitha
        Long houseId = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsDTO) {
            houseId = ((UserDetailsDTO) principal).getHouseId();
        }

        for (ESummonNotification notification : notifications) {

            if (houseId == 1) {
                if (StringUtils.isBlank(notification.getSessionNo())) {
                    // Convert entities to DTOs
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

                    InputInstance inputInstance = inputInstanceRepository
                            .findByApplicationNo(notification.getApplicationNo());
                    if (inputInstance != null) {
                        log.debug("Fetched InputInstance: {}", inputInstance);

                        // Map InputInstance to DTO and set in ESummonNotificationDTO
                        InputInstanceDTO inputInstanceDTO = InputInstanceMapper.toDTO(inputInstance);
                        dto.setInputInstanceDTO(inputInstanceDTO);
                    }

                    notificationDTOs.add(dto);
                }

            } else if (houseId == 2) {

                if (StringUtils.isBlank(notification.getRajyasabhaSessionNo())) {

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

                    InputInstance inputInstance = inputInstanceRepository
                            .findByApplicationNo(notification.getApplicationNo());
                    if (inputInstance != null) {
                        log.debug("Fetched InputInstance: {}", inputInstance);

                        // Map InputInstance to DTO and set in ESummonNotificationDTO
                        InputInstanceDTO inputInstanceDTO = InputInstanceMapper.toDTO(inputInstance);
                        dto.setInputInstanceDTO(inputInstanceDTO);
                    }

                    notificationDTOs.add(dto);
                }
            }

            // log.debug("Processing E-SummonNotification: {}", notification);
            // ESummonNotificationDTO dto = ESummonNotificationMapper.toDTO(notification);
            // ApplicationInstanceDTO instanceDTO =
            // fetchApplicationInstanceData(notification.getApplicationNo());
            // dto.setApplicationInstanceDTO(instanceDTO);
            // if (instanceDTO != null && instanceDTO.getId() != null) {
            // applicationPapersRepository.findByApplicationInstanceId(instanceDTO.getId())
            // .ifPresent(applicationPapers -> {
            // dto.setTemplateId(applicationPapers.getTemplateId());
            // dto.setTemplateHindiId(applicationPapers.getTemplateHindiId());
            // });
            // }
            // InputInstance inputInstance =
            // inputInstanceRepository.findByApplicationNo(notification.getApplicationNo());
            // if (inputInstance != null) {
            // log.debug("Fetched InputInstance: {}", inputInstance);
            // // Map InputInstance to DTO and set in ESummonNotificationDTO
            // InputInstanceDTO inputInstanceDTO = InputInstanceMapper.toDTO(inputInstance);
            // dto.setInputInstanceDTO(inputInstanceDTO);
            // }
            // notificationDTOs.add(dto);
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
        InputInstance inputInstance = inputInstanceRepository.findByApplicationNo(notification.getApplicationNo());
        if (inputInstance != null) {
            log.debug("Fetched InputInstance: {}", inputInstance);

            // Map to DTO
            InputInstanceDTO inputInstanceDTO = InputInstanceMapper.toDTO(inputInstance);
            dto.setInputInstanceDTO(inputInstanceDTO);
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

                        InputInstance inputInstance = inputInstanceRepository
                                .findByApplicationNo(summonNotification.getApplicationNo());
                        if (inputInstance != null) {
                            log.debug("Fetched InputInstance: {}", inputInstance);

                            // Map to DTO
                            InputInstanceDTO inputInstanceDTO = InputInstanceMapper.toDTO(inputInstance);
                            dto.setInputInstanceDTO(inputInstanceDTO);
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

            Optional<InputInstance> inputInstance = inputInstanceRepository.findByRefId(updatedNotification.getId());
            if (inputInstance != null) {
                InputInstance instance = inputInstance.get();
                instance.setCurrentStatus(updatedNotification.getStatus());
                instance.setHtmlContentEnglish(updatedNotification.getTemplateContent());
                instance.setHtmlContentHindi(updatedNotification.getTemplateContentHindi());
                instance = inputInstanceRepository.save(instance);
            }
            // Prepare the response DTO
            ESummonNotificationDTO responseDTO = ESummonNotificationMapper.toDTO(updatedNotification);

            // Now, let's automatically update the ApplicationInstance
            if (updatedNotification.getStatus() != null || updatedNotification.getEsummonIssuanceDate() != null) {
                ApplicationInstanceDTO applicationInstanceDTO = new ApplicationInstanceDTO();

                if (updatedNotification.getStatus() != null) {
                    applicationInstanceDTO.setCurrentStatus(updatedNotification.getStatus());
                    log.debug("Setting currentStatus: {}", updatedNotification.getStatus());
                }
                applicationInstanceDTO.setHtmlContentEnglish(updatedNotification.getTemplateContent());
                applicationInstanceDTO.setHtmlContentHindi(updatedNotification.getTemplateContentHindi());
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

            if (eSummonNotificationDTO.getTemplateContent() != null
                    || eSummonNotificationDTO.getTemplateContentHindi() != null) {
                Optional<ApplicationPapers> existingPapersOpt = applicationPapersRepository
                        .findByApplicationIdAndApplicationInstanceId(eSummonNotificationDTO.getApplicationId(),
                                responseDTO.getApplicationInstanceDTO().getId());

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

    // working code
//    @Override
//    public List<PersonalDetails> sendESummon(Long eSummonId, List<Long> memberIds, HttpServletRequest request) {
//        Optional<ESummonNotification> optionalNotification = eSummonNotificationRepository.findById(eSummonId);
//        if (!optionalNotification.isPresent()) {
//            throw new RuntimeException("eSummonNotification not found for id: " + eSummonId);
//        }
//
//        ESummonNotification eSummon = optionalNotification.get();
//        ESummonNotificationDTO dto = ESummonNotificationMapper.toDTO(eSummon);
//
//        // ✅ Extract Bearer token from Authorization header
//        String authorizationHeader = request.getHeader("Authorization");
//        String token;
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            token = authorizationHeader.substring(7);
//        } else {
//            throw new RuntimeException("Missing or invalid Authorization header");
//        }
//
//        // ✅ Prepare the upload HTML request
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.setBearerAuth(token);  // Dynamic Bearer token
//
//        System.out.println("##### dto.getTemplateContent() - " + dto.getTemplateContent());
//        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
//        formParams.add("htmlContentEn", dto.getTemplateContent());
//        formParams.add("startingPageNo", "1");
////        formParams.add("applicationId", String.valueOf(dto.getApplicationId()));
//        formParams.add("applicationId", "24");
//
//        HttpEntity<MultiValueMap<String, String>> formRequest = new HttpEntity<>(formParams, headers);
//
//        ResponseEntity<String> uploadResponse;
//        try {
//            uploadResponse = restTemplate.postForEntity(
//                    "https://dspstg.sansad.in/template/upload-html-english",
//                    formRequest,
//                    String.class
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to upload HTML content: " + e.getMessage(), e);
//        }
//
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode responseJson = objectMapper.readTree(uploadResponse.getBody());
//
//            if (!responseJson.has("documentUuid") || responseJson.get("documentUuid").isNull()) {
//                throw new RuntimeException("documentUuid not found in upload-html-english response");
//            }
//
//            UUID documentUuid = UUID.fromString(responseJson.get("documentUuid").asText());
//
//            System.out.println("### documentUuid - " + documentUuid);
//            // ✅ Save eSummon members
//            List<PersonalDetails> members = personalDetailsRepository.findAllById(memberIds);
//
//            List<ESummonMembers> summonMembersList = memberIds.stream()
//                    .map(memberId -> new ESummonMembers(eSummonId, memberId))
//                    .collect(Collectors.toList());
//            eSummonMembersRepository.saveAll(summonMembersList);
//
//            // ✅ Save eSummon history with document UUID
//            List<ESummonHistory> historyList = members.stream()
//                    .map(member -> ESummonHistory.builder()
//                            .eSummon(eSummon)
//                            .member(member)
//                            .documentUuid(documentUuid)
//                            .mailSentAt(LocalDateTime.now())
//                            .mailStatus("PENDING")
//                            .retryCount(0)
//                            .emailId(member.getEmailId())
//                            .build())
//                    .collect(Collectors.toList());
//            eSummonHistoryRepository.saveAll(historyList);
//
//            System.out.println("### saved successfully ! ");
//
//
//            // Optionally trigger async processing
//             eventProducerService.generateEvent(String.valueOf(eSummonId));
//
//            return members;
//
//        } catch (Exception ex) {
//            throw new RuntimeException("Failed to process upload-html-english response", ex);
//        }
//    }

@Override
public List<PersonalDetails> sendESummon(Long eSummonId, List<Long> memberIds, String token) {
    Optional<ESummonNotification> optionalNotification = eSummonNotificationRepository.findById(eSummonId);
    if (!optionalNotification.isPresent()) {
        throw new RuntimeException("eSummonNotification not found for id: " + eSummonId);
    }

    ESummonNotification eSummon = optionalNotification.get();
    ESummonNotificationDTO dto = ESummonNotificationMapper.toDTO(eSummon);

    // ✅ Use the passed token directly (no need to extract from request)
    if (token == null || token.isEmpty()) {
        throw new RuntimeException("Missing or invalid Authorization token");
    }

    // ✅ Prepare the upload HTML request
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setBearerAuth(token);  // Use passed Bearer token

    System.out.println("##### dto.getTemplateContent() - " + dto.getTemplateContent());
    MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
    formParams.add("htmlContentEn", dto.getTemplateContent());
    formParams.add("startingPageNo", "1");
    // formParams.add("applicationId", String.valueOf(dto.getApplicationId()));
    formParams.add("applicationId", "24");

    HttpEntity<MultiValueMap<String, String>> formRequest = new HttpEntity<>(formParams, headers);

    ResponseEntity<String> uploadResponse;
    try {
        uploadResponse = restTemplate.postForEntity(
                "https://dspstg.sansad.in/template/upload-html-english",
                formRequest,
                String.class
        );
    } catch (Exception e) {
        throw new RuntimeException("Failed to upload HTML content: " + e.getMessage(), e);
    }

    try {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(uploadResponse.getBody());

        if (!responseJson.has("documentUuid") || responseJson.get("documentUuid").isNull()) {
            throw new RuntimeException("documentUuid not found in upload-html-english response");
        }

        UUID documentUuid = UUID.fromString(responseJson.get("documentUuid").asText());

        System.out.println("### documentUuid - " + documentUuid);
        // ✅ Save eSummon members
        List<PersonalDetails> members = personalDetailsRepository.findAllById(memberIds);

        List<ESummonMembers> summonMembersList = memberIds.stream()
                .map(memberId -> new ESummonMembers(eSummonId, memberId))
                .collect(Collectors.toList());
        eSummonMembersRepository.saveAll(summonMembersList);

        // ✅ Save eSummon history with document UUID
        List<ESummonHistory> historyList = members.stream()
                .map(member -> ESummonHistory.builder()
                        .eSummon(eSummon)
                        .member(member)
                        .documentUuid(documentUuid)
                        .mailSentAt(LocalDateTime.now())
                        .mailStatus("PENDING")
                        .retryCount(0)
                        .emailId(member.getEmailId())
                        .build())
                .collect(Collectors.toList());
        eSummonHistoryRepository.saveAll(historyList);

        System.out.println("### saved successfully ! ");

        // Trigger Kafka event with token
        eventProducerService.generateEvent(eSummonId, token);

        return members;

    } catch (Exception ex) {
        throw new RuntimeException("Failed to process upload-html-english response", ex);
    }
}
}

