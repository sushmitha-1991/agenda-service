package com.dsp.agendaPapers.provisionalcalendarfinal.controller;

import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarGenerateDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarInputDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.dto.ProvisionalCalendarTableOfficeDTO;
import com.dsp.agendaPapers.provisionalcalendarfinal.service.ProvisionalCalendarService;
import com.dsp.agendaPapers.response.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendapapers/provisionalcalendar")
public class ProvisionalCalendarFinalController {

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
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<ProvisionalCalendarDTO>> createProvisionalCalendar(
            @Valid @RequestBody ProvisionalCalendarDTO provisionalCalendarDTO) {
        System.out.println("pc dto ");
        System.out.println(provisionalCalendarDTO);
        ProvisionalCalendarDTO createdCalendar = provisionalCalendarService.createProvisionalCalendar(provisionalCalendarDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new GenericResponse<>(true, "Provisional Calendar created successfully", createdCalendar, HttpStatus.CREATED.value()));
    }

    /**
     * Get Provisional Calendar by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get Provisional Calendar by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provisional Calendar retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Provisional Calendar not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<ProvisionalCalendarDTO>> getProvisionalCalendarById(@PathVariable Long id) {
        ProvisionalCalendarDTO calendarDTO = provisionalCalendarService.getProvisionalCalendarById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponse<>(true, "Provisional Calendar retrieved successfully", calendarDTO, HttpStatus.OK.value()));
    }

    /**
     * Get all Provisional Calendar entries.
     */
    @GetMapping("/getAll")
    @Operation(summary = "Get all Provisional Calendar entries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Provisional Calendar entries retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<List<ProvisionalCalendarDTO>>> getAllProvisionalCalendars() {
        List<ProvisionalCalendarDTO> calendarDTOs = provisionalCalendarService.getAllProvisionalCalendars();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponse<>(true, "All Provisional Calendar entries retrieved successfully", calendarDTOs, HttpStatus.OK.value()));
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
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<ProvisionalCalendarDTO>> updateProvisionalCalendar(
            @PathVariable Long id,
            @Valid @RequestBody ProvisionalCalendarDTO provisionalCalendarDTO) {
        ProvisionalCalendarDTO updatedCalendar = provisionalCalendarService.updateProvisionalCalendar(id, provisionalCalendarDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponse<>(true, "Provisional Calendar updated successfully", updatedCalendar, HttpStatus.OK.value()));
    }

    /**
     * Soft delete a Provisional Calendar entry.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a Provisional Calendar entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Provisional Calendar soft deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Provisional Calendar not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<Void>> softDeleteProvisionalCalendar(@PathVariable Long id) {
        provisionalCalendarService.softDeleteProvisionalCalendar(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new GenericResponse<>(true, "Provisional Calendar soft deleted successfully", null, HttpStatus.NO_CONTENT.value()));
    }

    @PostMapping("/input")
    public ProvisionalCalendarInputDTO createProvisionalCalendarInput(@RequestBody ProvisionalCalendarInputDTO inputDTO) {
        return provisionalCalendarService.createProvisionalCalendarInput(inputDTO);
    }

    @PostMapping("/table-office")
    public ProvisionalCalendarTableOfficeDTO createProvisionalCalendarTableOffice(@RequestBody ProvisionalCalendarTableOfficeDTO provisionalCalendarTableOfficeDTO) {
        return provisionalCalendarService.createProvisionalCalendarTableOffice(provisionalCalendarTableOfficeDTO);
    }

//    @PostMapping("/generate")
//    public ProvisionalCalendarGenerateDTO provisionalCalendarTableOfficeDTO(@RequestBody ProvisionalCalendarGenerateDTO provisionalCalendarGenerateDTO) {
//        return provisionalCalendarService.createProvisionalCalendarTableOffice(provisionalCalendarGenerateDTO);
//    }





}





