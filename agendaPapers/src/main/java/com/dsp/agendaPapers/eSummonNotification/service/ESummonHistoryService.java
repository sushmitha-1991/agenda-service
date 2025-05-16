package com.dsp.agendaPapers.eSummonNotification.service;



import com.dsp.agendaPapers.eSummonNotification.dto.ESummonHistoryDTO;
import com.dsp.agendaPapers.eSummonNotification.dto.ESummonMemberDetailsDTO;

import java.util.List;
import java.util.Optional;

public interface ESummonHistoryService {
    ESummonHistoryDTO createESummonHistory(ESummonHistoryDTO dto);
    ESummonHistoryDTO getESummonHistoryById(Long id);
    List<ESummonHistoryDTO> getAllESummonHistories();
    ESummonHistoryDTO updateESummonHistory(Long id, ESummonHistoryDTO dto);
    void deleteESummonHistory(Long id);
    Optional<ESummonHistoryDTO> findById(Long id);


    /**
            * Sends an eSummon for the given eSummonId to a list of memberIds and updates the history table.
     * @param eSummonId the id of the eSummonNotification
     * @param memberIds the list of member ids (PersonalDetails ids)
     * @return a list of ESummonHistoryDTO representing each history record created
     */
    List<ESummonHistoryDTO> sendESummonAndUpdateHistory(Long eSummonId, List<Long> memberIds);



    /**
     * Fetch all ESummonHistory records by eSummonId.
     * @param eSummonId the ID of the eSummon notification
     * @return List of ESummonHistoryDTO
     */
    List<ESummonHistoryDTO> getHistoriesByESummonId(Long eSummonId);

    void sendESummonAndCreateHistory(Long eSummonId, List<Long> memberIds);

    List<ESummonHistoryDTO> sendESummonToAllMembers(Long eSummonId);

    List<ESummonMemberDetailsDTO> getMemberDetailsByESummonId(Long eSummonId);


}

