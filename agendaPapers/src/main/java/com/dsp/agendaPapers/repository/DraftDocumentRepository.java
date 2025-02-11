/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.agendaPapers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.entity.DraftDocument;

import jakarta.transaction.Transactional;

/**
 * 
 */
@Repository
public interface DraftDocumentRepository extends JpaRepository<DraftDocument, Long> {

	/**
	 * @param applicationNo
	 * @return
	 */
	@Query("SELECT d.id FROM DraftDocument d WHERE d.applicationNo = :applicationNo")
	Long getIdByApplicationNo(@Param("applicationNo") String applicationNo);
	
    @Modifying
    @Transactional
    @Query("UPDATE DraftDocument d SET d.isDeleted = true, d.isActive = false WHERE d.id = :draftId")
    void softDeleteById(@Param("draftId") Long draftId);
}
