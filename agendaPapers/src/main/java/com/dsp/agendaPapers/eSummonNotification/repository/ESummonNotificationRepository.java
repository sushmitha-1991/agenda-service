package com.dsp.agendaPapers.eSummonNotification.repository;

import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ESummonNotificationRepository extends JpaRepository<ESummonNotification, Long> {

	List<ESummonNotification> findBySessionNo(String sessionNo);

}
