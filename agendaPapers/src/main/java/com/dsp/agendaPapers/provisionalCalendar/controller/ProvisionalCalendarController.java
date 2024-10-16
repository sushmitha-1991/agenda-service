package com.dsp.agendaPapers.provisionalCalendar.controller;

import com.dsp.agendaPapers.provisionalCalendar.Dto.ProvisionalCalendarDTO;
import com.dsp.agendaPapers.provisionalCalendar.service.ProvisionalCalendarService;
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
@RequestMapping("agendapapers/provisional-calendar")
public class ProvisionalCalendarController {

    @Autowired
    private ProvisionalCalendarService provisionalCalendarService;

    @PostMapping("/create")
    @Operation(summary = "Create a new provisional calendar entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Provisional calendar created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<ProvisionalCalendarDTO>> createProvisionalCalendar(@Valid @RequestBody ProvisionalCalendarDTO provisionalCalendarDTO) {
        ProvisionalCalendarDTO createdProvisionalCalendar = provisionalCalendarService.createProvisionalCalendar(provisionalCalendarDTO);
        GenericResponse<ProvisionalCalendarDTO> response = new GenericResponse<>(
                true, // success should be a boolean
                "Provisional calendar created successfully", // message should be a String
                createdProvisionalCalendar, // data should be of type T (ProvisionalCalendarDTO)
                HttpStatus.CREATED.value() // status code
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get provisional calendar by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provisional calendar retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Provisional calendar not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<ProvisionalCalendarDTO>> getProvisionalCalendarById(@PathVariable Long id) {
        ProvisionalCalendarDTO provisionalCalendarDTO = provisionalCalendarService.getProvisionalCalendarById(id);
        GenericResponse<ProvisionalCalendarDTO> response = new GenericResponse<>(
                true, // success should be a boolean
                "Provisional calendar retrieved successfully", // message should be a String
                provisionalCalendarDTO, // data should be of type T (ProvisionalCalendarDTO)
                HttpStatus.OK.value() // status code
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get all provisional calendars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provisional calendars retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<List<ProvisionalCalendarDTO>>> getAllProvisionalCalendars() {
        List<ProvisionalCalendarDTO> provisionalCalendarDTOs = provisionalCalendarService.getAllProvisionalCalendars();
        GenericResponse<List<ProvisionalCalendarDTO>> response = new GenericResponse<>(
                true, // success should be a boolean
                "Provisional calendars retrieved successfully", // message should be a String
                provisionalCalendarDTOs, // data should be of type T (List<ProvisionalCalendarDTO>)
                HttpStatus.OK.value() // status code
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing provisional calendar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provisional calendar updated successfully"),
            @ApiResponse(responseCode = "404", description = "Provisional calendar not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<ProvisionalCalendarDTO>> updateProvisionalCalendar(@PathVariable Long id, @Valid @RequestBody ProvisionalCalendarDTO provisionalCalendarDTO) {
        ProvisionalCalendarDTO updatedProvisionalCalendar = provisionalCalendarService.updateProvisionalCalendar(id, provisionalCalendarDTO);
        GenericResponse<ProvisionalCalendarDTO> response = new GenericResponse<>(
                true, // success should be a boolean
                "Provisional calendar updated successfully", // message should be a String
                updatedProvisionalCalendar, // data should be of type T (ProvisionalCalendarDTO)
                HttpStatus.OK.value() // status code
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a provisional calendar by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Provisional calendar deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Provisional calendar not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<Void>> deleteProvisionalCalendar(@PathVariable Long id) {
        provisionalCalendarService.deleteProvisionalCalendar(id);
        GenericResponse<Void> response = new GenericResponse<>(
                true, // success should be a boolean
                "Provisional calendar deleted successfully", // message should be a String
                null, // data is null for delete response
                HttpStatus.NO_CONTENT.value() // status code
        );
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
