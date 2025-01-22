/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.agendaPapers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.entity.WorkflowHistory;

/**
 * 
 */
@Repository
public interface WorkflowHistoryRepository extends JpaRepository<WorkflowHistory, Long> {

}
