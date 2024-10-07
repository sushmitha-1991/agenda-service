package com.dsp.agendaPapers.eSummonNotification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;

@Repository
public interface ESummonNotificationRepository extends JpaRepository<ESummonNotification, Long> {

}
