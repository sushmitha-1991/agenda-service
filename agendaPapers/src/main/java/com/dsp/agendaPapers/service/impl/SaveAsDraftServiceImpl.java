/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.agendaPapers.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsp.agendaPapers.dto.ApplicationInstanceFilterDTO;
import com.dsp.agendaPapers.dto.ApplicationInstanceResponseDTO;
import com.dsp.agendaPapers.dto.SaveAsDraftDTO;
import com.dsp.agendaPapers.entity.ApplicationInstance;
import com.dsp.agendaPapers.entity.ApplicationInstanceDraftStatus;
import com.dsp.agendaPapers.mapper.ApplicationInstanceMapper;
import com.dsp.agendaPapers.mapper.SaveAsDraftMapper;
import com.dsp.agendaPapers.repository.ApplicationInstanceDraftStatusRepository;
import com.dsp.agendaPapers.repository.ApplicationInstanceRepository;
import com.dsp.agendaPapers.service.SaveAsDraftService;

import jakarta.transaction.Transactional;

@Service
public class SaveAsDraftServiceImpl implements SaveAsDraftService {

	@Autowired
	private ApplicationInstanceRepository applicationInstanceRepository;

	@Autowired
	private ApplicationInstanceDraftStatusRepository draftStatusRepository;

	@Autowired
	private SaveAsDraftMapper saveAsDraftMapper;

	@Autowired
	private ApplicationInstanceMapper applicationInstanceMapper;

	@Transactional
	@Override
	public Long saveAsDraft(SaveAsDraftDTO saveAsDraftDTO) {
		// Map and save ApplicationInstance
		ApplicationInstance applicationInstance = saveAsDraftMapper.toApplicationInstance(saveAsDraftDTO);
		ApplicationInstance savedApplicationInstance = applicationInstanceRepository.save(applicationInstance);

		// Map and save ApplicationInstanceDraftStatus
		ApplicationInstanceDraftStatus draftStatus = saveAsDraftMapper.toDraftStatus(saveAsDraftDTO,
				savedApplicationInstance.getId());
		draftStatusRepository.save(draftStatus);

		// Return the ID of the saved ApplicationInstance
		return savedApplicationInstance.getId();
	}

	@Override
	public List<ApplicationInstanceResponseDTO> getDrafts(ApplicationInstanceFilterDTO filterDTO) {
		List<ApplicationInstance> entities;

		// Case 1: lokSabhaSession, lokSabhaSessionNumber, and applicationNo are
		// provided
		if (filterDTO.getLokSabhaSession() != null && filterDTO.getLokSabhaSessionNumber() != null
				&& filterDTO.getApplicationNo() != null) {
			entities = applicationInstanceRepository.findByLokSabhaFilters(filterDTO.getLokSabhaSession(),
					filterDTO.getLokSabhaSessionNumber(), filterDTO.getApplicationNo());
		}
		// Case 2: rajyaSabhaSession and applicationNo are provided
		else if (filterDTO.getRajyaSabhaSession() != null && filterDTO.getApplicationNo() != null) {
			entities = applicationInstanceRepository.findByRajyaSabhaFilters(filterDTO.getRajyaSabhaSession(),
					filterDTO.getApplicationNo());
		}
		// If neither case matches, return empty list or throw an exception
		else {
			entities = List.of(); // Or throw some validation exception
		}

		// Convert entities to DTOs
		return applicationInstanceMapper.toDTOs(entities);
	}

	@Override
	public ApplicationInstanceDraftStatus getDraftStatusByApplicationInstanceId(Long applicationInstanceId) {
		return draftStatusRepository.findByApplicationInstanceId(applicationInstanceId);
	}

}
