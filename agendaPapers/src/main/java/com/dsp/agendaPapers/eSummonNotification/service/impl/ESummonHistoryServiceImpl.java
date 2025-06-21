package com.dsp.agendaPapers.eSummonNotification.service.impl;


import com.dsp.agendaPapers.eSummonNotification.dto.ESummonHistoryDTO;
import com.dsp.agendaPapers.eSummonNotification.dto.ESummonMemberDetailsDTO;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonHistory;
import com.dsp.agendaPapers.eSummonNotification.entity.PersonalDetails;
import com.dsp.agendaPapers.eSummonNotification.mapper.ESummonHistoryMapper;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonHistoryRepository;
import com.dsp.agendaPapers.eSummonNotification.repository.PersonalDetailsRepository;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonNotificationRepository;
import com.dsp.agendaPapers.eSummonNotification.service.ESummonHistoryService;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ESummonHistoryServiceImpl implements ESummonHistoryService {

    private final ESummonHistoryRepository eSummonHistoryRepository;
    private final ESummonNotificationRepository eSummonNotificationRepository;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final ESummonHistoryMapper eSummonHistoryMapper;
    private final RestTemplate restTemplate;


    @Override
    @Transactional
    public ESummonHistoryDTO createESummonHistory(ESummonHistoryDTO dto) {
//        log.info("Creating ESummonHistory with DTO: {}", dto);

        if (dto.getESummonId() == null || dto.getMemberId() == null) {
            throw new IllegalArgumentException("ESummon ID and Member ID cannot be null");
        }

        // Fetch ESummonNotification entity
        ESummonNotification eSummon = eSummonNotificationRepository.findById(dto.getESummonId())
                .orElseThrow(() -> new ResourceNotFoundException("ESummon not found with ID: " + dto.getESummonId()));

        // Fetch PersonalDetails entity
        PersonalDetails member = personalDetailsRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + dto.getMemberId()));

        // Map DTO to entity
        ESummonHistory eSummonHistory = eSummonHistoryMapper.toEntity(dto, eSummon, member);
        eSummonHistory = eSummonHistoryRepository.save(eSummonHistory);

//        log.info("ESummonHistory saved successfully with ID: {}", eSummonHistory.getId());

        // Convert back to DTO and return
        return eSummonHistoryMapper.toDTO(eSummonHistory);
    }

//    @Override
//    public ESummonHistoryDTO getESummonHistoryById(Long id) {
//        if (id == null) {
//            throw new IllegalArgumentException("ID cannot be null");
//        }
//
//        ESummonHistory entity = eSummonHistoryRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("E-Summon history not found"));
//        return eSummonHistoryMapper.toDTO(entity);
//    }

    @Override
    public List<ESummonHistoryDTO> getAllESummonHistories() {
        return eSummonHistoryRepository.findAll()
                .stream().map(eSummonHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ESummonHistoryDTO updateESummonHistory(Long id, ESummonHistoryDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        ESummonHistory existingEntity = eSummonHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("E-Summon history not found"));

        // Update entity properties from DTO
        existingEntity.setMailStatus(dto.getMailStatus());
        existingEntity.setRetryCount(dto.getRetryCount());
        existingEntity.setRemarks(dto.getRemarks());

        // You can update other fields as needed (e.g. updatedBy, updatedAt)
        // existingEntity.setUpdatedBy(dto.getUpdatedBy());
        // existingEntity.setUpdatedAt(dto.getUpdatedAt());

        return eSummonHistoryMapper.toDTO(eSummonHistoryRepository.save(existingEntity));
    }


    @Override
    public void deleteESummonHistory(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        ESummonHistory entity = eSummonHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("E-Summon history not found"));

        // Soft delete logic
        entity.setIsDeleted(true);
        eSummonHistoryRepository.save(entity);
    }

    // for the all the data
    @Override
    public ESummonHistoryDTO getESummonHistoryById(Long id) {
        // Fetch the entity by id, using Optional
        ESummonHistory eSummonHistory = eSummonHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ESummonHistory not found for ID: " + id));

        // Convert the entity to DTO
        return eSummonHistoryMapper.toDTO(eSummonHistory);
    }

    // for the all the data


    @Override
    public Optional<ESummonHistoryDTO> findById(Long id) {
        Optional<ESummonHistory> entity = eSummonHistoryRepository.findById(id);
        return entity.map(eSummonHistoryMapper::toDTO);
    }

    @Override
    public List<ESummonHistoryDTO> sendESummonAndUpdateHistory(Long eSummonId, List<Long> memberIds) {
        // Retrieve the eSummonNotification by id
        Optional<ESummonNotification> optionalNotification = eSummonNotificationRepository.findById(eSummonId);
        if (!optionalNotification.isPresent()) {
            throw new RuntimeException("eSummonNotification not found for id: " + eSummonId);
        }
        ESummonNotification notification = optionalNotification.get();

        // Retrieve member details using the provided memberIds
        List<PersonalDetails> members = personalDetailsRepository.findAllById(memberIds);
        List<ESummonHistoryDTO> historyDTOs = new ArrayList<>();

        // Process each member
        for (PersonalDetails member : members) {
            // Simulate sending the eSummon (for example, send an email/notification)
            System.out.println("Sending eSummon (ID: " + eSummonId + ") to member: "
                    + member.getFullName() + ", Email: " + member.getEmailId());

            // Create a new history record for the current member
            ESummonHistory history = ESummonHistory.builder()
                    .eSummon(notification)   // set associated eSummonNotification
                    .member(member)          // set the member details
                    .documentUuid(UUID.randomUUID())
                    .mailSentAt(LocalDateTime.now())
                    .mailStatus("SENT")
                    .retryCount(0)
                    .remarks("Email sent successfully")
                    .build();

            // Save the history record
            ESummonHistory savedHistory = eSummonHistoryRepository.save(history);
            // Map the saved entity to its DTO
            ESummonHistoryDTO dto = eSummonHistoryMapper.toDTO(savedHistory);
            historyDTOs.add(dto);
        }
        return historyDTOs;
    }

    // get by sumon id
    @Override
    public List<ESummonHistoryDTO> getHistoriesByESummonId(Long eSummonId) {
        List<ESummonHistory> histories = eSummonHistoryRepository.findByeSummon_Id(eSummonId);
        return histories.stream()
                .map(eSummonHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void sendESummonAndCreateHistory(Long eSummonId, List<Long> memberIds) {
        // Fetch eSummon Notification
        ESummonNotification eSummon = eSummonNotificationRepository.findById(eSummonId)
                .orElseThrow(() -> new ResourceNotFoundException("ESummon not found with ID: " + eSummonId));

        // Fetch all member details
        List<PersonalDetails> members = personalDetailsRepository.findAllById(memberIds);

        List<ESummonHistory> historyList = new ArrayList<>();

        for (PersonalDetails member : members) {
            // Simulate email sending
            System.out.println("Sending eSummon to: " + member.getFullName() + " at " + member.getEmailId());

            // Create DTO (if needed) or directly create entity
            ESummonHistoryDTO dto = new ESummonHistoryDTO();
            dto.setESummonId(eSummonId);
            dto.setMemberId(member.getId());
            dto.setMailSentAt(LocalDateTime.now());
            dto.setMailStatus("SENT"); // or "PENDING", "FAILED"
            dto.setRetryCount(0);
            dto.setRemarks("Mail sent successfully");
            dto.setEmailId(member.getEmailId());
            dto.setDocumentUuid(UUID.randomUUID()); // Or pass actual document UUID if available

            // Convert to entity
            ESummonHistory history = eSummonHistoryMapper.toEntity(dto, eSummon, member);
            historyList.add(history);
        }

        // Save all history records in batch
        eSummonHistoryRepository.saveAll(historyList);
    }

// not working code
//    @Override
//    @Transactional
//    public List<ESummonHistoryDTO> sendESummonToAllMembers(Long eSummonId) {
//
//        ESummonNotification eSummon = eSummonNotificationRepository.findById(eSummonId)
//                .orElseThrow(() -> new ResourceNotFoundException("eSummon not found with ID: " + eSummonId));
//
//        List<Long> memberIds = personalDetailsRepository.findMemberIdsByESummonId(eSummonId);
//
//        List<PersonalDetails> members = personalDetailsRepository.findAllById(memberIds);
//
//        List<ESummonHistory> historyList = new ArrayList<>();
//
//        for (PersonalDetails member : members) {
//            System.out.println("Sending email to: " + member.getFullName() + ", Email: " + member.getEmailId());
//
//            ESummonHistory history = ESummonHistory.builder()
//                    .eSummon(eSummon)
//                    .member(member)
//                    .emailId(member.getEmailId())
//                    .mailStatus("SENT") // You can update based on actual email send result
//                    .mailSentAt(LocalDateTime.now())
//                    .retryCount(0)
//                    .remarks("Email sent successfully")
//                    .build();
//
//            historyList.add(history);
//        }
//
//        eSummonHistoryRepository.saveAll(historyList);
//
//        return historyList.stream()
//                .map(eSummonHistoryMapper::toDTO)
//                .collect(Collectors.toList());
//    }

// working code v1
//    @Override
//    @Transactional
//    public List<ESummonHistoryDTO> sendESummonToAllMembers(Long eSummonId) {
//
//        ESummonNotification eSummon = eSummonNotificationRepository.findById(eSummonId)
//                .orElseThrow(() -> new ResourceNotFoundException("eSummon not found with ID: " + eSummonId));
//
//        List<Long> memberIds = personalDetailsRepository.findMemberIdsByESummonId(eSummonId);
//        List<PersonalDetails> members = personalDetailsRepository.findAllById(memberIds);
//
//        List<ESummonHistory> historyList = new ArrayList<>();
//
//        for (PersonalDetails member : members) {
//            // Skip if email is null or blank
//            if (member.getEmailId() == null || member.getEmailId().isBlank()) {
//                System.out.println("Skipping member with null/blank email: " + member.getFullName());
//                continue;
//            }
//
//            System.out.println("Sending email to: " + member.getFullName() + ", Email: " + member.getEmailId());
//
//            ESummonHistory history = ESummonHistory.builder()
//                    .eSummon(eSummon)
//                    .member(member)
//                    .emailId(member.getEmailId())
//                    .mailStatus("SENT")
//                    .mailSentAt(LocalDateTime.now())
//                    .retryCount(0)
//                    .remarks("Email sent successfully")
//                    .documentUuid(UUID.randomUUID()) // Ensures not-null value
//                    .build();
//
//            historyList.add(history);
//        }
//
//        if (!historyList.isEmpty()) {
//            eSummonHistoryRepository.saveAll(historyList);
//        }
//
//        return historyList.stream()
//                .map(eSummonHistoryMapper::toDTO)
//                .collect(Collectors.toList());
//    }

    // v2 new code for the using kafka
    @Override
    @Transactional
    public List<ESummonHistoryDTO> sendESummonToAllMembers(Long eSummonId) {
        ESummonNotification eSummon = eSummonNotificationRepository.findById(eSummonId)
                .orElseThrow(() -> new ResourceNotFoundException("eSummon not found with ID: " + eSummonId));

        List<Long> memberIds = personalDetailsRepository.findMemberIdsByESummonId(eSummonId);
        List<PersonalDetails> members = personalDetailsRepository.findAllById(memberIds);

        List<ESummonHistoryDTO> resultList = new ArrayList<>();

        for (PersonalDetails member : members) {
            try {
                if (member.getEmailId() == null || member.getEmailId().isBlank()) {
                    System.out.println("Skipping member with null/blank email: " + member.getFullName());
                    continue;
                }

                // Check if email already sent
                Optional<ESummonHistory> existingHistoryOpt = eSummonHistoryRepository.findByESummonAndMember(eSummon, member);
                ESummonHistory history;

                if (existingHistoryOpt.isPresent()) {
                    history = existingHistoryOpt.get();
                    if ("SENT".equalsIgnoreCase(history.getMailStatus())) {
                        System.out.println("Email already sent to: " + member.getEmailId());
                        continue;
                    }
                } else {
                    // Create initial pending record
                    history = ESummonHistory.builder()
                            .eSummon(eSummon)
                            .member(member)
                            .emailId(member.getEmailId())
                            .mailSentAt(LocalDateTime.now())
                            .mailStatus("PENDING")
                            .retryCount(0)
                            .documentUuid(UUID.randomUUID())
                            .remarks("Pending send")
                            .build();

                    history = eSummonHistoryRepository.save(history);
                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return resultList;
    }



    // working code
    @Override
    public List<ESummonMemberDetailsDTO> getMemberDetailsByESummonId(Long eSummonId) {
        List<Object[]> results = eSummonHistoryRepository.getMemberDetailsByESummonId(eSummonId);

        Map<Long, ESummonMemberDetailsDTO> uniqueMembers = new HashMap<>();
//        Map<Long, ESummonMemberDetailsDTO> uniqueMembers = new LinkedHashMap<>();


        for (Object[] obj : results) {
            Long memberId = obj[0] != null ? ((Number) obj[0]).longValue() : null;

            // Only add if not already present
            if (!uniqueMembers.containsKey(memberId)) {
                ESummonMemberDetailsDTO dto = new ESummonMemberDetailsDTO();
                dto.setMemberId(memberId);
                dto.setFullName((String) obj[1]);
                dto.setProfilePicture((UUID) obj[2]);
                dto.setPartyName((String) obj[3]);
                dto.setPartyAbbr((String) obj[4]);
                dto.setConstituencyName((String) obj[5]);
                dto.setConstituencyAbbr((String) obj[6]);
                dto.setStateName((String) obj[7]);
                dto.setMailStatus((String) obj[8]);
                dto.setDocumentUuid((String) obj[9]); // first document UUID
                dto.setDocumentUuidHindi(obj[10] != null ? UUID.fromString(obj[10].toString()) : null);
                uniqueMembers.put(memberId, dto);
            }
        }

        return new ArrayList<>(uniqueMembers.values());
    }

}






