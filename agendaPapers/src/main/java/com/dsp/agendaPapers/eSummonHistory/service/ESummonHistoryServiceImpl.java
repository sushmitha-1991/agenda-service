package com.dsp.agendaPapers.eSummonHistory.service;


import com.dsp.agendaPapers.eSummonHistory.dto.ESummonHistoryDTO;
import com.dsp.agendaPapers.eSummonHistory.entity.ESummonHistory;
import com.dsp.agendaPapers.eSummonHistory.entity.PersonalDetails;
import com.dsp.agendaPapers.eSummonHistory.mapper.ESummonHistoryMapper;
import com.dsp.agendaPapers.eSummonHistory.repository.ESummonHistoryRepository;
import com.dsp.agendaPapers.eSummonHistory.repository.PersonalDetailsRepository;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonNotificationRepository;
import com.dsp.agendaPapers.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ESummonHistoryServiceImpl implements ESummonHistoryService {

    private final ESummonHistoryRepository eSummonHistoryRepository;
    private final ESummonNotificationRepository eSummonNotificationRepository;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final ESummonHistoryMapper eSummonHistoryMapper;





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
}








