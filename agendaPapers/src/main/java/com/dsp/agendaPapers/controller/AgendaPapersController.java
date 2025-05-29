/**
 * 
 * @author Mrinal Lakhanpal
 * @version 1.0
 */
package com.dsp.agendaPapers.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.agendaPapers.dto.ApplicationInstanceFilterDTO;
import com.dsp.agendaPapers.dto.ApplicationInstanceResponseDTO;
import com.dsp.agendaPapers.dto.SaveAsDraftDTO;
import com.dsp.agendaPapers.entity.ApplicationInstanceDraftStatus;
import com.dsp.agendaPapers.response.GenericResponse;
import com.dsp.agendaPapers.service.SaveAsDraftService;

/**
 * 
 */
@RestController
@RequestMapping()
public class AgendaPapersController {

	@Autowired
	private SaveAsDraftService saveAsDraftService;

	@PostMapping("/save-as-draft")
	public ResponseEntity<GenericResponse<Map<String, Long>>> saveAsDraft(@RequestBody SaveAsDraftDTO saveAsDraftDTO) {
		Long applicationInstanceId = saveAsDraftService.saveAsDraft(saveAsDraftDTO);

		// Prepare the data object with the ID
		Map<String, Long> responseData = Map.of("id", applicationInstanceId);

		GenericResponse<Map<String, Long>> response = new GenericResponse<>(true, "Data saved as draft successfully",
				responseData, HttpStatus.OK.value());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/application-instance/get-drafts")
	public ResponseEntity<GenericResponse<List<ApplicationInstanceResponseDTO>>> getDrafts(
			@RequestBody ApplicationInstanceFilterDTO filterDTO) {
		List<ApplicationInstanceResponseDTO> drafts = saveAsDraftService.getDrafts(filterDTO);
		GenericResponse<List<ApplicationInstanceResponseDTO>> response = new GenericResponse<>(true,
				"Drafts retrieved successfully", drafts, HttpStatus.OK.value());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-draft-status/{applicationInstanceId}")
	public ResponseEntity<GenericResponse<ApplicationInstanceDraftStatus>> getDraftStatusByApplicationInstanceId(
			@PathVariable Long applicationInstanceId) {
		ApplicationInstanceDraftStatus draftStatus = saveAsDraftService
				.getDraftStatusByApplicationInstanceId(applicationInstanceId);

		if (draftStatus == null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new GenericResponse<>(false, "Draft status not found", null, HttpStatus.OK.value()));
		}

		return ResponseEntity.ok(
				new GenericResponse<>(true, "Draft status retrieved successfully", draftStatus, HttpStatus.OK.value()));
	}

}
