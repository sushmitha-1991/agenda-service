package com.dsp.agendaPapers.eSummonHistory.service;



import com.dsp.agendaPapers.eSummonHistory.dto.ESummonHistoryDTO;

import java.util.List;
import java.util.Optional;

public interface ESummonHistoryService {
    ESummonHistoryDTO createESummonHistory(ESummonHistoryDTO dto);
    ESummonHistoryDTO getESummonHistoryById(Long id);
    List<ESummonHistoryDTO> getAllESummonHistories();
    ESummonHistoryDTO updateESummonHistory(Long id, ESummonHistoryDTO dto);
    void deleteESummonHistory(Long id);
    Optional<ESummonHistoryDTO> findById(Long id);

}

