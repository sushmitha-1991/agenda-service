package com.dsp.agendaPapers.eSummonNotification.service;

import com.dsp.agendaPapers.eSummonNotification.entity.PersonalDetails;
import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;
import jakarta.servlet.http.HttpServletRequest;


import java.util.List;



public interface ESummonNotificationService {

	ESummonNotificationDTO saveESummonNotification(ESummonNotificationDTO eSummonNotificationDTO);

	List<ESummonNotificationDTO> getAllESummonNotifications();

	ESummonNotificationDTO getESummonNotificationById(Long id);

	void deleteESummonNotification(Long id);

	ESummonNotificationDTO updateESummonNotification(Long id, ESummonNotificationDTO eSummonNotificationDTO);

	List<ESummonNotificationDTO> getAllESummonNotificationBySessionNo(String sessionNo);

//	List<PersonalDetails> sendESummon(Long eSummonId, List<Long> memberIds,HttpServletRequest httpRequest);

	List<PersonalDetails> sendESummon(Long eSummonId, List<Long> memberIds, String token);




}


