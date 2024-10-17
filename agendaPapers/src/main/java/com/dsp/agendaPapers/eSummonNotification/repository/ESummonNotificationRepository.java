package com.dsp.agendaPapers.eSummonNotification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.summonNotification.entity.SummonNotification;

@Repository
public interface ESummonNotificationRepository extends JpaRepository<ESummonNotification, Long> {

	List<ESummonNotification> findBySessionNo(String sessionNo);

}
