package com.dsp.agendaPapers.eSummonHistory.repository;


import com.dsp.agendaPapers.eSummonHistory.entity.ESummonHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ESummonHistoryRepository extends JpaRepository<ESummonHistory, Long> {

    // Correct return type as Optional<ESummonHistory>
    Optional<ESummonHistory> findById(Long id);
}

