package com.dsp.agendaPapers.eSummonNotification.controller;

import java.util.List;
import java.util.UUID;

import com.dsp.agendaPapers.eSummonNotification.dto.SendESummonRequest;
import com.dsp.agendaPapers.eSummonNotification.entity.PersonalDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/agendapapers/esummon")
public class ESummonNotificationController {

	@Autowired
	private ESummonNotificationService eSummonNotificationService;

	private static final Logger log = LoggerFactory.getLogger(ESummonNotificationController.class);

	@Operation(summary = "Create a new E-SummonNotification", description = "Creates a new E-SummonNotification and returns the created E-SummonNotification details.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "E-Summon created successfully."),
			@ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PostMapping("/create")
	public ResponseEntity<GenericResponse<ESummonNotificationDTO>> createSummonNotification(
			@RequestBody ESummonNotificationDTO eSummonNotificationDTO) {
		ESummonNotificationDTO savedNotification = eSummonNotificationService
				.saveESummonNotification(eSummonNotificationDTO);
		System.out.println("############" + eSummonNotificationDTO);
		GenericResponse<ESummonNotificationDTO> response = new GenericResponse<>(true,
				"E-Summon created successfully.", savedNotification, HttpStatus.CREATED.value());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Operation(summary = "Get all E-SummonNotifications", description = "Retrieves a list of all E-Summon Notifications.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "E-Summon Notifications retrieved successfully"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/getAll")
	public ResponseEntity<GenericResponse<List<ESummonNotificationDTO>>> getAllESummonNotifications() {
		List<ESummonNotificationDTO> eSummonNotificationDTOs = eSummonNotificationService.getAllESummonNotifications();
		GenericResponse<List<ESummonNotificationDTO>> response = new GenericResponse<>(true,
				"E-Summon notifications retrieved successfully", eSummonNotificationDTOs, HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@Operation(summary = "Get E-summonNotification By SessionNo", description = "Retrieve all E-summonNotification By its SessionNo.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "E-SummonNotification retrieved successfully"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/get/sessionNo/{sessionNo}")
	public ResponseEntity<GenericResponse<List<ESummonNotificationDTO>>> getAllESummonNotificationBySessionNo(@PathVariable String sessionNo) {
		List<ESummonNotificationDTO> eSummonNotificationDTO = eSummonNotificationService.getAllESummonNotificationBySessionNo(sessionNo);
		GenericResponse<List<ESummonNotificationDTO>> response = new GenericResponse<>(true,
				"E-Summon notifications retrieved successfully", eSummonNotificationDTO, HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Get E-SummonNotification By Id", description = "Retrieve E-SummonNotification by its Id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "E-SummonNotification retrieved successfully"),
			@ApiResponse(responseCode = "404", description = "E-SummonNotification not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/get/{id}")
	public ResponseEntity<GenericResponse<ESummonNotificationDTO>> getESummonNotificationById(@PathVariable Long id) {
		ESummonNotificationDTO eSummonNotificationDTO = eSummonNotificationService.getESummonNotificationById(id);
		GenericResponse<ESummonNotificationDTO> response = new GenericResponse<>(true,
				"Retrieved E-SummonNotification By Id", eSummonNotificationDTO, HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Update E-SummonNotification", description = "Updates an E-Summon Notification by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "E-Summon Notification updated successfully"),
			@ApiResponse(responseCode = "404", description = "E-Summon Notification not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PutMapping("/update/{id}")
	public ResponseEntity<GenericResponse<ESummonNotificationDTO>> updateSummonNotification(@PathVariable Long id,
			@RequestBody ESummonNotificationDTO eSummonNotificationDTO) {
		ESummonNotificationDTO updatedSummonNotification = eSummonNotificationService.updateESummonNotification(id,
				eSummonNotificationDTO);
		GenericResponse<ESummonNotificationDTO> response = new GenericResponse<>(true,
				"E-Summon Notification updated successfully", updatedSummonNotification, HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Delete an E-SummonNotification by ID", description = "Deletes a specific E-SummonNotification by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "E-SummonNotification deleted successfully"),
			@ApiResponse(responseCode = "404", description = "E-SummonNotification not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<GenericResponse<String>> deleteESummonNotification(@PathVariable Long id) {
		eSummonNotificationService.deleteESummonNotification(id);
		GenericResponse<String> response = new GenericResponse<>(true, "E-SummonNotification deleted successfully",
				null, HttpStatus.OK.value());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
// new code
	@PostMapping("/send-esummon")
	public ResponseEntity<?> sendESummon(@RequestBody SendESummonRequest request, HttpServletRequest httpRequest) {
		try {
			String authHeader = httpRequest.getHeader("Authorization");
			String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;

			// Call service with token
			List<PersonalDetails> memberDetails = eSummonNotificationService.sendESummonV2(request.getId(), request.getMembers(), token);

			GenericResponse<List<PersonalDetails>> response = new GenericResponse<>(
					true,
					"eSummon sent successfully.",
					memberDetails,
					HttpStatus.OK.value()
			);
			return ResponseEntity.ok(response);

		} catch (Exception ex) {
			GenericResponse<?> response = new GenericResponse<>(
					false,
					"Error: " + ex.getMessage(),
					null,
					HttpStatus.BAD_REQUEST.value()
			);
			return ResponseEntity.badRequest().body(response);
		}
	}


}
