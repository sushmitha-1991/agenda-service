package com.dsp.agendaPapers.provisionalcalendarfinal.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarGenerateDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarInputDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarTableOfficeDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberRequest;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.SerialNumberResponse;
import com.dsp.agendaPapers.provisionalcalendarfinal.feignclient.BearerClient;
import com.dsp.agendaPapers.provisionalcalendarfinal.service.ProvisionalCalendarService;
import com.dsp.agendaPapers.response.GenericResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/agendapapers/provisionalcalendar")
public class ProvisionalCalendarFinalController {

	@Autowired
	private BearerClient bearerClient;

	@Autowired
	private ProvisionalCalendarService provisionalCalendarService;

	/**
	 * Create a new Provisional Calendar entry.
	 */
	@PostMapping("/create")
	@Operation(summary = "Create a new Provisional Calendar entry")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Provisional Calendar created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<ProvisionalCalendarDTO>> createProvisionalCalendar(
			@Valid @RequestBody ProvisionalCalendarDTO provisionalCalendarDTO) {
		ProvisionalCalendarDTO createdCalendar = provisionalCalendarService
				.createProvisionalCalendar(provisionalCalendarDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse<>(true,
				"Provisional Calendar created successfully", createdCalendar, HttpStatus.CREATED.value()));
	}

	/**
	 * Get Provisional Calendar by ID.
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Get Provisional Calendar by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Provisional Calendar retrieved successfully"),
			@ApiResponse(responseCode = "404", description = "Provisional Calendar not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<ProvisionalCalendarDTO>> getProvisionalCalendarById(@PathVariable Long id) {
		ProvisionalCalendarDTO calendarDTO = provisionalCalendarService.getProvisionalCalendarById(id);
		return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(true,
				"Provisional Calendar retrieved successfully", calendarDTO, HttpStatus.OK.value()));
	}

	/**
	 * Get all Provisional Calendar entries.
	 */
	@GetMapping("/getAll")
	@Operation(summary = "Get all Provisional Calendar entries")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "All Provisional Calendar entries retrieved successfully"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<List<ProvisionalCalendarDTO>>> getAllProvisionalCalendars() {
		List<ProvisionalCalendarDTO> calendarDTOs = provisionalCalendarService.getAllProvisionalCalendars();
		return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(true,
				"All Provisional Calendar entries retrieved successfully", calendarDTOs, HttpStatus.OK.value()));
	}

	/**
	 * Update an existing Provisional Calendar entry.
	 */
	@PutMapping("/{id}")
	@Operation(summary = "Update an existing Provisional Calendar")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Provisional Calendar updated successfully"),
			@ApiResponse(responseCode = "404", description = "Provisional Calendar not found"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<ProvisionalCalendarDTO>> updateProvisionalCalendar(@PathVariable Long id,
			@Valid @RequestBody ProvisionalCalendarDTO provisionalCalendarDTO) {
		ProvisionalCalendarDTO updatedCalendar = provisionalCalendarService.updateProvisionalCalendar(id,
				provisionalCalendarDTO);
		return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(true,
				"Provisional Calendar updated successfully", updatedCalendar, HttpStatus.OK.value()));
	}
	/**
	 * Soft delete a Provisional Calendar entry.
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "Soft delete a Provisional Calendar entry")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Provisional Calendar soft deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Provisional Calendar not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<Void>> softDeleteProvisionalCalendar(@PathVariable Long id) {
		provisionalCalendarService.softDeleteProvisionalCalendar(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new GenericResponse<>(true,
				"Provisional Calendar soft deleted successfully", null, HttpStatus.NO_CONTENT.value()));
	}

	@PostMapping("/input")
	@Operation(summary = "Create a new Provisional Calendar Input entry")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Provisional Calendar Input created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<ProvisionalCalendarInputDTO>> createProvisionalCalendarInput(
			@Valid @RequestBody ProvisionalCalendarInputDTO inputDTO) {
		

		ProvisionalCalendarInputDTO createdInput = provisionalCalendarService.createProvisionalCalendarInput(inputDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse<>(true,
				"Provisional Calendar Input created successfully", createdInput, HttpStatus.CREATED.value()));
	}

	@PostMapping("/table-office")
	@Operation(summary = "Create a new Provisional Calendar Table Office entry")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Provisional Calendar Table Office created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<ProvisionalCalendarTableOfficeDTO>> createProvisionalCalendarTableOffice(
			@Valid @RequestBody ProvisionalCalendarTableOfficeDTO provisionalCalendarTableOfficeDTO) {
		ProvisionalCalendarTableOfficeDTO createdTableOffice = provisionalCalendarService
				.createProvisionalCalendarTableOffice(provisionalCalendarTableOfficeDTO);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new GenericResponse<>(true, "Provisional Calendar Table Office created successfully",
						createdTableOffice, HttpStatus.CREATED.value()));
	}

	@PostMapping("/generate")
	@Operation(summary = "Generate a new Provisional Calendar entry")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Provisional Calendar generated successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<ProvisionalCalendarGenerateDTO>> createProvisionalCalendarGenerate(
			@Valid @RequestBody ProvisionalCalendarGenerateDTO provisionalCalendarGenerateDTO) {
		// for ref id

		ProvisionalCalendarGenerateDTO createdGenerate = provisionalCalendarService
				.createProvisionalCalendarGenerate(provisionalCalendarGenerateDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(new GenericResponse<>(true,
				"Provisional Calendar generated successfully", createdGenerate, HttpStatus.CREATED.value()));
	}

	// for the input status

	@PutMapping("/update-inputstatus/{id}")
	@Operation(summary = "Update input status and remarks of a Provisional Calendar entry")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Input status and remarks updated successfully"),
			@ApiResponse(responseCode = "404", description = "Provisional Calendar entry not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<ProvisionalCalendarInputDTO>> updateInputStatus(@PathVariable Long id,
			@RequestParam String inputStatus, @RequestParam(required = false) String remarks) {

		if (inputStatus == null || inputStatus.isEmpty()) {
			throw new IllegalArgumentException("InputStatus is required and cannot be null or empty.");
		}

		ProvisionalCalendarInputDTO updatedDTO = provisionalCalendarService.updateInputStatus(id, inputStatus, remarks);

		return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(true,
				"Provisional Calendar Input updated successfully", updatedDTO, HttpStatus.OK.value()));
	}

	// for generated the ref id

	@PostMapping("/test")
	public SerialNumberResponse getSerialNumber(@RequestBody SerialNumberRequest serialNumberRequest) {
		return bearerClient.getResource(serialNumberRequest);
	}

	// get ALl with filter I am checking for newly deployed Working code

//	@GetMapping("/getAllfilter")
//	@Operation(summary = "Get all Provisional Calendar entries between specific dates and by submittedBy")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "All Provisional Calendar entries retrieved successfully"),
//			@ApiResponse(responseCode = "404", description = "No Provisional Calendar entries found"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public ResponseEntity<GenericResponse<List<ProvisionalCalendarDTO>>> getAllProvisionalCalendars(
//			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//			@RequestParam(required = false) String inputStatus, @RequestParam(required = false) String submittedBy) {
//		System.out.println("filter $$$$$");
//
//		try {
//
//			List<ProvisionalCalendarDTO> calendarDTOs = provisionalCalendarService
//					.getAllProvisionalCalendarsBetweenDates(startDate, endDate, inputStatus, submittedBy);
//			System.out.println(calendarDTOs);
//
//			if (calendarDTOs == null || calendarDTOs.isEmpty()) {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse<>(true,
//						"No Provisional Calendar entries found", calendarDTOs, HttpStatus.NOT_FOUND.value()));
//			}
//
//			return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(true,
//					"All Provisional Calendar entries retrieved successfully", calendarDTOs, HttpStatus.OK.value()));
//		} catch (RuntimeException ex) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body(new GenericResponse<>(false, "An error occurred while fetching Provisional Calendar entries",
//							null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
//		}
//	}


	@GetMapping("/getAllfilter")
	@Operation(summary = "Get all Provisional Calendar entries between specific dates and by submittedBy")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "All Provisional Calendar entries retrieved successfully"),
			@ApiResponse(responseCode = "404", description = "No Provisional Calendar entries found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<List<ProvisionalCalendarDTO>>> getAllProvisionalCalendars(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) String inputStatus,
			@RequestParam(required = false) String submittedBy
	) {
		try {
			// Fetch filtered provisional calendar entries
			List<ProvisionalCalendarDTO> calendarDTOs = provisionalCalendarService
					.getAllProvisionalCalendarsBetweenDates(startDate, endDate, inputStatus, submittedBy);

			// Check if the list is empty or null
			if (calendarDTOs == null || calendarDTOs.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new GenericResponse<>(
								true,
								"No Provisional Calendar entries found",
								calendarDTOs,
								HttpStatus.NOT_FOUND.value()
						));
			}

			// Return a successful response with the data
			return ResponseEntity.status(HttpStatus.OK)
					.body(new GenericResponse<>(
							true,
							"All Provisional Calendar entries retrieved successfully",
							calendarDTOs,
							HttpStatus.OK.value()
					));
		} catch (RuntimeException ex) {
			// Log the exception for debugging
			ex.printStackTrace();

			// Return an internal server error response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new GenericResponse<>(
							false,
							"An error occurred while fetching Provisional Calendar entries",
							null,
							HttpStatus.INTERNAL_SERVER_ERROR.value()
					));
		}
	}

}
