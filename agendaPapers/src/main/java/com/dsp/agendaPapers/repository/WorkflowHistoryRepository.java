/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.agendaPapers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.entity.WorkflowHistory;

import jakarta.transaction.Transactional;

/**
 * 
 */
@Repository
public interface WorkflowHistoryRepository extends JpaRepository<WorkflowHistory, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE from workflow.workflow_history where draft_id = :draftId", nativeQuery=true)    
    void deleteByDraftId(Long draftId);

}
