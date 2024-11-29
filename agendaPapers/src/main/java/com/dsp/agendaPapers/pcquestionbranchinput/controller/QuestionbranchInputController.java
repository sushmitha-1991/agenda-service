//package com.dsp.agendaPapers.pcquestionbranchinput.controller;
//
//import com.dsp.agendaPapers.pcquestionbranchinput.Dto.QuestionbranchInputDTO;
//import com.dsp.agendaPapers.pcquestionbranchinput.service.QuestionbranchInputService;
//import com.dsp.agendaPapers.response.GenericResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/agendapapers/tableoffice-input")
//public class QuestionbranchInputController {
//
//    @Autowired
//    private QuestionbranchInputService questionbranchInputService;
//
//    @PostMapping("/create")
//    @Operation(summary = "Create a new question branch input entry")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Table office input created successfully"),
//            @ApiResponse(responseCode = "400", description = "Invalid input data"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<GenericResponse<QuestionbranchInputDTO>> createQuestionbranchInput(@Valid @RequestBody QuestionbranchInputDTO questionbranchInputDTO) {
//        QuestionbranchInputDTO createdQuestionbranchInput = questionbranchInputService.createQuestionbranchInput(questionbranchInputDTO);
//        GenericResponse<QuestionbranchInputDTO> response = new GenericResponse<>(
//                true, // success should be a boolean
//                "Table office input created successfully", // message should be a String
//                createdQuestionbranchInput, // data should be of type T (QuestionbranchInputDTO)
//                HttpStatus.CREATED.value() // status code
//        );
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{id}")
//    @Operation(summary = "Get question branch input by ID")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Table office input retrieved successfully"),
//            @ApiResponse(responseCode = "404", description = "Question branch input not found"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<GenericResponse<QuestionbranchInputDTO>> getQuestionbranchInputById(@PathVariable Long id) {
//        QuestionbranchInputDTO questionbranchInputDTO = questionbranchInputService.getQuestionbranchInputById(id);
//        GenericResponse<QuestionbranchInputDTO> response = new GenericResponse<>(
//                true, // success should be a boolean
//                "Table office input retrieved successfully", // message should be a String
//                questionbranchInputDTO, // data should be of type T (QuestionbranchInputDTO)
//                HttpStatus.OK.value() // status code
//        );
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @GetMapping("/getAll")
//    @Operation(summary = "Get all question branch inputs")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Table office all inputs retrieved successfully"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<GenericResponse<List<QuestionbranchInputDTO>>> getAllQuestionbranchInputs() {
//        List<QuestionbranchInputDTO> questionbranchInputDTOs = questionbranchInputService.getAllQuestionbranchInputs();
//        GenericResponse<List<QuestionbranchInputDTO>> response = new GenericResponse<>(
//                true, // success should be a boolean
//                "Table office all inputs retrieved successfully", // message should be a String
//                questionbranchInputDTOs, // data should be of type T (List<QuestionbranchInputDTO>)
//                HttpStatus.OK.value() // status code
//        );
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @PutMapping("/{id}")
//    @Operation(summary = "Update an existing question branch input")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Table office input updated successfully"),
//            @ApiResponse(responseCode = "404", description = "Question branch input not found"),
//            @ApiResponse(responseCode = "400", description = "Invalid input data"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<GenericResponse<QuestionbranchInputDTO>> updateQuestionbranchInput(@PathVariable Long id, @Valid @RequestBody QuestionbranchInputDTO questionbranchInputDTO) {
//        QuestionbranchInputDTO updatedQuestionbranchInput = questionbranchInputService.updateQuestionbranchInput(id, questionbranchInputDTO);
//        GenericResponse<QuestionbranchInputDTO> response = new GenericResponse<>(
//                true, // success should be a boolean
//                "Table office input updated successfully", // message should be a String
//                updatedQuestionbranchInput, // data should be of type T (QuestionbranchInputDTO)
//                HttpStatus.OK.value() // status code
//        );
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    @Operation(summary = "Delete a question branch input by ID")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "Table Office input deleted successfully"),
//            @ApiResponse(responseCode = "404", description = "Question branch input not found"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<GenericResponse<Void>> deleteQuestionbranchInput(@PathVariable Long id) {
//        questionbranchInputService.deleteQuestionbranchInput(id);
//        GenericResponse<Void> response = new GenericResponse<>(
//                true, // success should be a boolean
//                "Table Office input deleted successfully", // message should be a String
//                null, // data is null for delete response
//                HttpStatus.NO_CONTENT.value() // status code
//        );
//        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
//    }
//
//    @GetMapping("/groupchartsLS/{groupchartsLS}")
//    @Operation(summary = "Get question branch inputs by groupchartsLS")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Question branch inputs retrieved successfully"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<GenericResponse<List<QuestionbranchInputDTO>>> getQuestionbranchInputsByGroupchartsLS(@PathVariable String groupchartsLS) {
//        List<QuestionbranchInputDTO> questionbranchInputDTOs = questionbranchInputService.getQuestionbranchInputsByGroupchartsLS(groupchartsLS);
//        GenericResponse<List<QuestionbranchInputDTO>> response = new GenericResponse<>(
//                true, // success should be a boolean
//                "Question branch inputs retrieved successfully", // message should be a String
//                questionbranchInputDTOs, // data should be of type T (List<QuestionbranchInputDTO>)
//                HttpStatus.OK.value() // status code
//        );
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @GetMapping("/groupchartsRS/{groupchartsRS}")
//    @Operation(summary = "Get question branch inputs by groupchartsRS")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Question branch inputs retrieved successfully"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<GenericResponse<List<QuestionbranchInputDTO>>> getQuestionbranchInputsByGroupchartsRS(@PathVariable String groupchartsRS) {
//        List<QuestionbranchInputDTO> questionbranchInputDTOs = questionbranchInputService.getQuestionbranchInputsByGroupchartsRS(groupchartsRS);
//        GenericResponse<List<QuestionbranchInputDTO>> response = new GenericResponse<>(
//                true, // success should be a boolean
//                "Question branch inputs retrieved successfully", // message should be a String
//                questionbranchInputDTOs, // data should be of type T (List<QuestionbranchInputDTO>)
//                HttpStatus.OK.value() // status code
//        );
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//}
