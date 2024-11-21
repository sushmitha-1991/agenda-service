package com.dsp.agendaPapers.provisionalcalendarinput.controller;

import com.dsp.agendaPapers.provisionalcalendarinput.Dto.ProvisionalCalendarInputDTO;
import com.dsp.agendaPapers.provisionalcalendarinput.service.ProvisionalCalendarInputService;
import com.dsp.agendaPapers.response.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agendapapers/provisional-input")
public class ProvisionalCalendarInputController {


    @Autowired
    private ProvisionalCalendarInputService provisionalCalendarInputService;


    @PostMapping("/create")
    @Operation(summary = "Create a new provisional calendar input")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Provisional calendar input created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<ProvisionalCalendarInputDTO>> createProvisionalCalendarInput(
            @Valid @RequestBody ProvisionalCalendarInputDTO provisionalCalendarInputDTO) {

        ProvisionalCalendarInputDTO createdProvisionalCalendar = provisionalCalendarInputService.createProvisionalCalendarInput(provisionalCalendarInputDTO);

        GenericResponse<ProvisionalCalendarInputDTO> response = new GenericResponse<>(
                true,
                "Provisional calendar created successfully",
                createdProvisionalCalendar,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get provisional calendar input by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provisional calendar input retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Provisional calendar input not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<ProvisionalCalendarInputDTO>> getProvisionalCalendarInputById(@PathVariable Long id) {
        ProvisionalCalendarInputDTO dto = provisionalCalendarInputService.getProvisionalCalendarInputById(id);
        GenericResponse<ProvisionalCalendarInputDTO> response = new GenericResponse<>(true, "Input retrieved successfully", dto, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PutMapping("/{id}")
    @Operation(summary = "Update an existing provisional calendar input")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provisional calendar input updated successfully"),
            @ApiResponse(responseCode = "404", description = "Provisional calendar input not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<ProvisionalCalendarInputDTO>> updateProvisionalCalendarInput(@PathVariable Long id, @Valid @RequestBody ProvisionalCalendarInputDTO dto) {
        ProvisionalCalendarInputDTO updatedInput = provisionalCalendarInputService.updateProvisionalCalendarInput(id, dto);
        GenericResponse<ProvisionalCalendarInputDTO> response = new GenericResponse<>(true, "Input updated successfully", updatedInput, HttpStatus.OK.value());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a provisional calendar input by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Provisional calendar input deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Provisional calendar input not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<Void>> deleteProvisionalCalendarInput(@PathVariable Long id) {
        provisionalCalendarInputService.deleteProvisionalCalendarInput(id);
        GenericResponse<Void> response = new GenericResponse<>(true, "Input deleted successfully", null, HttpStatus.NO_CONTENT.value());
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }


    @GetMapping("/getAll")
    @Operation(summary = "Get all or filtered provisional calendar inputs")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provisional calendar inputs retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<List<ProvisionalCalendarInputDTO>>> getAllProvisionalCalendarInputs(
            @RequestParam(required = false) String submittedByBranch,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        List<ProvisionalCalendarInputDTO> inputs;

        if (submittedByBranch != null && status != null && startDate != null && endDate != null) {
            // Parse date strings to Date objects
            Date start = Date.valueOf(startDate);
            Date end = Date.valueOf(endDate);
            inputs = provisionalCalendarInputService.getProvisionalData(submittedByBranch, status, start, end);
        } else {
            inputs = provisionalCalendarInputService.getAllProvisionalCalendarInputs();
        }

        GenericResponse<List<ProvisionalCalendarInputDTO>> response = new GenericResponse<>(
                true,
                "Inputs retrieved successfully",
                inputs,
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



        @GetMapping("/generatestatus")
        @Operation(summary = "Generate data by status")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Data fetched successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid request"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        public ResponseEntity<GenericResponse<List<ProvisionalCalendarInputDTO>>> generateByStatus(
                @RequestParam List<String> statuses) {

            // Fetch data by statuses
            List<ProvisionalCalendarInputDTO> inputs = provisionalCalendarInputService.getProvisionalInputsByStatus(statuses);

            // Wrap response in GenericResponse
            GenericResponse<List<ProvisionalCalendarInputDTO>> response = new GenericResponse<>(
                    true,
                    "Data fetched successfully",
                    inputs,
                    HttpStatus.OK.value()
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }





