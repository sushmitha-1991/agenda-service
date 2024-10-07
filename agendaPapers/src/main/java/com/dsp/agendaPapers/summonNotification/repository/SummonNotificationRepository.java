package com.dsp.agendaPapers.summonNotification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.summonNotification.entity.SummonNotification;

@Repository
public interface SummonNotificationRepository extends JpaRepository<SummonNotification, Long> {

}
