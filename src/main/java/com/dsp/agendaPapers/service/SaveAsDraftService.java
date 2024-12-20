/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.agendaPapers.service;

import java.util.List;

import com.dsp.agendaPapers.dto.ApplicationInstanceFilterDTO;
import com.dsp.agendaPapers.dto.ApplicationInstanceResponseDTO;
import com.dsp.agendaPapers.dto.SaveAsDraftDTO;
import com.dsp.agendaPapers.entity.ApplicationInstanceDraftStatus;

/**
 * 
 */
public interface SaveAsDraftService {
	Long saveAsDraft(SaveAsDraftDTO saveAsDraftDTO);
	
    List<ApplicationInstanceResponseDTO> getDrafts(ApplicationInstanceFilterDTO filterDTO);

	/**
	 * @param applicationInstanceId
	 * @return
	 */
	ApplicationInstanceDraftStatus getDraftStatusByApplicationInstanceId(Long applicationInstanceId);


}
