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

import java.util.List;
@RestController
@RequestMapping("/agendapapers/provisional-input")
public class ProvisionalCalendarInputController {


    @Autowired
    private ProvisionalCalendarInputService provisionalCalendarInputService;

    @PostMapping("/create")
    @Operation(summary = "Create a new provisional calendar input entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Provisional calendar input created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<ProvisionalCalendarInputDTO>> createProvisionalCalendarInput(@Valid @RequestBody ProvisionalCalendarInputDTO provisionalCalendarInputDTO) {
        ProvisionalCalendarInputDTO createdProvisionalCalendarInput = provisionalCalendarInputService.createProvisionalCalendarInput(provisionalCalendarInputDTO);
        GenericResponse<ProvisionalCalendarInputDTO> response = new GenericResponse<>(
                true, // success should be a boolean
                "Provisional calendar input created successfully", // message should be a String
                createdProvisionalCalendarInput, // data should be of type T (ProvisionalCalendarInputDTO)
                HttpStatus.CREATED.value() // status code
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get provisional calendar input by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provisional calendar input retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Provisional calendar input not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<ProvisionalCalendarInputDTO>> getProvisionalCalendarInputById(@PathVariable Long id) {
        ProvisionalCalendarInputDTO provisionalCalendarInputDTO = provisionalCalendarInputService.getProvisionalCalendarInputById(id);
        GenericResponse<ProvisionalCalendarInputDTO> response = new GenericResponse<>(
                true, // success should be a boolean
                "Provisional calendar input retrieved successfully", // message should be a String
                provisionalCalendarInputDTO, // data should be of type T (ProvisionalCalendarInputDTO)
                HttpStatus.OK.value() // status code
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get all provisional calendar inputs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provisional calendar inputs retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<List<ProvisionalCalendarInputDTO>>> getAllProvisionalCalendarInputs() {
        List<ProvisionalCalendarInputDTO> provisionalCalendarInputDTOs = provisionalCalendarInputService.getAllProvisionalCalendarInputs();
        GenericResponse<List<ProvisionalCalendarInputDTO>> response = new GenericResponse<>(
                true, // success should be a boolean
                "Provisional calendar inputs retrieved successfully", // message should be a String
                provisionalCalendarInputDTOs, // data should be of type T (List<ProvisionalCalendarInputDTO>)
                HttpStatus.OK.value() // status code
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing provisional calendar input")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provisional calendar input updated successfully"),
            @ApiResponse(responseCode = "404", description = "Provisional calendar input not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<ProvisionalCalendarInputDTO>> updateProvisionalCalendarInput(@PathVariable Long id, @Valid @RequestBody ProvisionalCalendarInputDTO provisionalCalendarInputDTO) {
        ProvisionalCalendarInputDTO updatedProvisionalCalendarInput = provisionalCalendarInputService.updateProvisionalCalendarInput(id, provisionalCalendarInputDTO);
        GenericResponse<ProvisionalCalendarInputDTO> response = new GenericResponse<>(
                true, // success should be a boolean
                "Provisional calendar input updated successfully", // message should be a String
                updatedProvisionalCalendarInput, // data should be of type T (ProvisionalCalendarInputDTO)
                HttpStatus.OK.value() // status code
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a provisional calendar input by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Provisional calendar input deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Provisional calendar input not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GenericResponse<Void>> deleteProvisionalCalendarInput(@PathVariable Long id) {
        provisionalCalendarInputService.deleteProvisionalCalendarInput(id);
        GenericResponse<Void> response = new GenericResponse<>(
                true, // success should be a boolean
                "Provisional calendar input deleted successfully", // message should be a String
                null, // data is null for delete response
                HttpStatus.NO_CONTENT.value() // status code
        );
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
