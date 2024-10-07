package com.dsp.agendaPapers.summonNotification.service;

import java.util.List;

import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;
import com.dsp.agendaPapers.summonNotification.entity.SummonNotification;

import jakarta.validation.Valid;

public interface SummonNotificationService {

	
	//SummonNotificationDTO createSummonNotification(@Valid SummonNotificationDTO summonNotificationDTO);

	
	List<SummonNotificationDTO> getAllSummonNotifications();


	SummonNotificationDTO getSummonNotificationById(Long id);


	SummonNotificationDTO saveSummonNotification(SummonNotificationDTO summonNotificationDTO);


	SummonNotificationDTO updateSummonNotification(Long id, SummonNotificationDTO summonNotificationDTO);


	void deleteSummonNotification(Long id);


	



}
