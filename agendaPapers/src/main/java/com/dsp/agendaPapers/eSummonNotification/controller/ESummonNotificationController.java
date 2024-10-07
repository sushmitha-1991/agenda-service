package com.dsp.agendaPapers.eSummonNotification.controller;

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

import com.dsp.agendaPapers.eSummonNotification.dto.ESummonNotificationDTO;
import com.dsp.agendaPapers.eSummonNotification.service.ESummonNotificationService;
import com.dsp.agendaPapers.response.GenericResponse;
import com.dsp.agendaPapers.summonNotification.dto.SummonNotificationDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
@RestController
@RequestMapping("agendapapers/esummon")
public class ESummonNotificationController {
	
	@Autowired
	private ESummonNotificationService eSummonNotificationService;
	
	@Operation(summary = "Create a new E-SummonNotification", description = "Creates a new E-SummonNotification and returns the created E-SummonNotification details.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "E-SummonNotification created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PostMapping("/create")
	public ResponseEntity<ESummonNotificationDTO> createSummonNotification(@RequestBody ESummonNotificationDTO eSummonNotificationDTO) {
		ESummonNotificationDTO savedNotification = eSummonNotificationService.saveESummonNotification(eSummonNotificationDTO);
		return null;
		
	}
	@GetMapping("/getAll")
	public ResponseEntity<GenericResponse<List<ESummonNotificationDTO>>> getAllESummonNotifications() {
		List<ESummonNotificationDTO> eSummonNOtificationDTOs = eSummonNotificationService.getAllESummonNotifications();
		GenericResponse<List<ESummonNotificationDTO>> response = new GenericResponse<>(true,
				"E-Summon notifications retrieved successfully", eSummonNOtificationDTOs, HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@Operation(summary = "Get E-summonNotification By Id", description = "Retrieve all E-summonNotification By its Id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "E-SummonNotification retrieved successfully"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/get/{id}")
	public ResponseEntity<GenericResponse<ESummonNotificationDTO>> getESummonNotificationById(@PathVariable Long id) {
		ESummonNotificationDTO summonNotificationDTO = eSummonNotificationService.getESummonNotificationById(id);
		GenericResponse<ESummonNotificationDTO> response = new GenericResponse<>(true,
				"Retrive E-summonNotification By Id", summonNotificationDTO, HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Delete an E-SummonNotication by ID", description = "Deletes a specific E-SummonNotification by its ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "E-SummonNotification deleted successfully"),
			@ApiResponse(responseCode = "404", description = "E-SummonNotification not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GenericResponse<String>> deleteESummonNotification(@PathVariable Long id) {
		eSummonNotificationService.deleteESummonNotification(id);
		GenericResponse<String> response = new GenericResponse<>(true,
				"SummonNotification and associated ApplicationInstance deleted successfully", null,
				HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@Operation(summary = "Update summonNotification", description = "Updates a Summon Notification by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Summon Notification updated successfully"),
			@ApiResponse(responseCode = "404", description = "Summon Notification not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PutMapping("/update/{id}")
	public ResponseEntity<GenericResponse<ESummonNotificationDTO>> updateSummonNotification(@PathVariable Long id,
			@RequestBody ESummonNotificationDTO eSummonNotificationDTO) {
		ESummonNotificationDTO updatedSummonNotification = eSummonNotificationService.updateESummonNotification(id,
				eSummonNotificationDTO);
		GenericResponse<ESummonNotificationDTO> response = new GenericResponse<>(true,
				"Summon Notification updated successfully", updatedSummonNotification, HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
