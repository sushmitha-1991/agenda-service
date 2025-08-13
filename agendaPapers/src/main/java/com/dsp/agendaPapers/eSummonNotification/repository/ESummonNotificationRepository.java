package com.dsp.agendaPapers.eSummonNotification.repository;

import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ESummonNotificationRepository extends JpaRepository<ESummonNotification, Long> {

	List<ESummonNotification> findBySessionNo(String sessionNo);

	@Query("select e.createdAt from ESummonNotification e where e.id = :id")
	Optional<LocalDateTime> findCreatedAtById(Long id);


	interface DocumentListView {
		List<Map<String, Object>> getDocumentList();
	}

	@Query("select e.documentList as documentList from ESummonNotification e where e.id = :id")
	Optional<DocumentListView> findDocumentListViewById(Long id);

}
