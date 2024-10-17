package com.dsp.agendaPapers.eSummonNotification.service;

import java.util.List;

import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;
import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;


public interface ESummonNotificationService {

	ESummonNotificationDTO saveESummonNotification(ESummonNotificationDTO eSummonNotificationDTO);

	List<ESummonNotificationDTO> getAllESummonNotifications();

	ESummonNotificationDTO getESummonNotificationById(Long id);

	void deleteESummonNotification(Long id);

	ESummonNotificationDTO updateESummonNotification(Long id, ESummonNotificationDTO eSummonNotificationDTO);

	List<ESummonNotificationDTO> getAllESummonNotificationBySessionNo(String sessionNo);

}
