package com.dsp.agendaPapers.prorogationnotice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.agendaPapers.prorogationnotice.Dto.ProrogationNoticeDTO;
import com.dsp.agendaPapers.prorogationnotice.service.ProrogationNoticeService;
import com.dsp.agendaPapers.response.GenericResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/agendapapers/prorogationNotice")
public class ProrogationNoticeController {
	
	@Autowired
	private ProrogationNoticeService prorogationNoticeService;
	
	@Operation(summary = "Create a new ProrogationNotice", description = "Creates a new ProrogationNotice and returns the created SummonNotification details.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "ProrogationNotice created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PostMapping("/create")
	public ResponseEntity<GenericResponse<ProrogationNoticeDTO>> createProrogationNotice(
			@RequestBody ProrogationNoticeDTO prorogationNoticeDTO) {
		ProrogationNoticeDTO savedNotification = prorogationNoticeService
				.saveProrogationNotice(prorogationNoticeDTO);
		GenericResponse<ProrogationNoticeDTO> response = new GenericResponse<>(true,
				"ProrogationNotice created successfully", savedNotification, HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Operation(summary = "Get propagationNotice By Id", description = "Retrieve all propagationNotice By its Id.")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "PropagationNotice retrieved successfully"),
	        @ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/get/{id}")
	public ResponseEntity<GenericResponse<ProrogationNoticeDTO>> getPropagationNoticeById(@PathVariable Long id) {
		ProrogationNoticeDTO propagationNoticeDTO = prorogationNoticeService.getPropagationNoticeById(id);
	    GenericResponse<ProrogationNoticeDTO> response = new GenericResponse<>(true,
	            "Retrieved propagationNotice By Id", propagationNoticeDTO, HttpStatus.OK.value());
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
