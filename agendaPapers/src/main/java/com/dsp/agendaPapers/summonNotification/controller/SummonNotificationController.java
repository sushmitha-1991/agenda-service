package com.dsp.agendaPapers.summonNotification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dsp.agendaPapers.response.GenericResponse;
import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;
import com.dsp.agendaPapers.summonNotification.entity.SummonNotification;
import com.dsp.agendaPapers.summonNotification.service.SummonNotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("agendapapers/summonNotification")
public class SummonNotificationController {

	@Autowired
	private SummonNotificationService summonNotificationService;

	@Autowired
	private RestTemplate restTemplate;

	@Operation(summary = "Create a new SummonNotification", description = "Creates a new SummonNotification and returns the created SummonNotification details.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "SummonNotification created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PostMapping("/create")
	public ResponseEntity<SummonNotificationDTO> createSummonNotification(
			@RequestBody SummonNotificationDTO summonNotificationDTO) {
		SummonNotificationDTO savedNotification = summonNotificationService
				.saveSummonNotification(summonNotificationDTO);
		return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
	}

	@Operation(summary = "Get all summonNotification", description = "Retrieves a list of all Summon Notification.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Summon Notification retrieved successfully"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/getAll")
	public ResponseEntity<GenericResponse<List<SummonNotificationDTO>>> getAllSummonNotifications() {
		List<SummonNotificationDTO> summonNOtificationDTOs = summonNotificationService.getAllSummonNotifications();
		GenericResponse<List<SummonNotificationDTO>> response = new GenericResponse<>(true,
				"Summon notifications retrieved successfully", summonNOtificationDTOs, HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Get summonNotification By Id", description = "Retrieve all summonNotification By its Id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "SummonNotification retrieved successfully"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/get/{id}")
	public ResponseEntity<GenericResponse<SummonNotificationDTO>> getSummonNotificationById(@PathVariable Long id) {
		SummonNotificationDTO summonNotificationDTO = summonNotificationService.getSummonNotificationById(id);
		GenericResponse<SummonNotificationDTO> response = new GenericResponse<>(true,
				"Retrive summonNotification By Id", summonNotificationDTO, HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Update summonNotification", description = "Updates a Summon Notification by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Summon Notification updated successfully"),
			@ApiResponse(responseCode = "404", description = "Summon Notification not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PutMapping("/update/{id}")
	public ResponseEntity<GenericResponse<SummonNotificationDTO>> updateSummonNotification(@PathVariable Long id,
			@RequestBody SummonNotificationDTO summonNotificationDTO) {
		SummonNotificationDTO updatedSummonNotification = summonNotificationService.updateSummonNotification(id,
				summonNotificationDTO);
		GenericResponse<SummonNotificationDTO> response = new GenericResponse<>(true,
				"Summon Notification updated successfully", updatedSummonNotification, HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Delete an application by ID", description = "Deletes a specific application by its ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Application deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Application not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GenericResponse<String>> deleteSummonNotification(@PathVariable Long id) {
		summonNotificationService.deleteSummonNotification(id);
		GenericResponse<String> response = new GenericResponse<>(true,
				"SummonNotification and associated ApplicationInstance deleted successfully", null,
				HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
