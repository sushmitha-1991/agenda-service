package com.dsp.agendaPapers.eSummonHistory.controller;


import com.dsp.agendaPapers.eSummonHistory.dto.ESummonHistoryDTO;
import com.dsp.agendaPapers.eSummonHistory.service.ESummonHistoryService;
import com.dsp.agendaPapers.eSummonHistory.service.ESummonHistoryServiceImpl;
import com.dsp.agendaPapers.eSummonNotification.dto.SendESummonRequest;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.service.ESummonNotificationService;
import com.dsp.agendaPapers.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agendapapers/summon-history")
@RequiredArgsConstructor
public class ESummonHistoryController {

    private final ESummonHistoryService eSummonHistoryService;

    @Autowired
    private ESummonNotificationService eSummonNotificationService;


    // ✅ Create an E-Summon History entry
    @PostMapping("/create")
    public ResponseEntity<GenericResponse<ESummonHistoryDTO>> createESummonHistory(@RequestBody ESummonHistoryDTO dto) {
        if (dto.getESummonId() == null || dto.getMemberId() == null) {
            return ResponseEntity.badRequest().body(new GenericResponse<>(false,
                    "ESummon ID and Member ID cannot be null", null, HttpStatus.BAD_REQUEST.value()));
        }

        ESummonHistoryDTO createdHistory = eSummonHistoryService.createESummonHistory(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GenericResponse<>(true, "E-Summon history created successfully", createdHistory, HttpStatus.CREATED.value()));
    }

    // ✅ Get E-Summon History by ID
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<ESummonHistoryDTO>> getESummonHistoryById(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new GenericResponse<>(false,
                    "ID cannot be null", null, HttpStatus.BAD_REQUEST.value()));
        }

        ESummonHistoryDTO history = eSummonHistoryService.getESummonHistoryById(id);
        return ResponseEntity.ok(new GenericResponse<>(true, "E-Summon history fetched successfully", history, HttpStatus.OK.value()));
    }

    // ✅ Get All E-Summon Histories
    @GetMapping("/getAll")
    public ResponseEntity<GenericResponse<List<ESummonHistoryDTO>>> getAllESummonHistories() {
        List<ESummonHistoryDTO> historyList = eSummonHistoryService.getAllESummonHistories();
        return ResponseEntity.ok(new GenericResponse<>(true, "E-Summon history list fetched successfully", historyList, HttpStatus.OK.value()));
    }

    // ✅ Update an existing E-Summon History entry
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<ESummonHistoryDTO>> updateESummonHistory(@PathVariable Long id, @RequestBody ESummonHistoryDTO dto) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new GenericResponse<>(false,
                    "ID cannot be null", null, HttpStatus.BAD_REQUEST.value()));
        }

        ESummonHistoryDTO updatedHistory = eSummonHistoryService.updateESummonHistory(id, dto);
        return ResponseEntity.ok(new GenericResponse<>(true, "E-Summon history updated successfully", updatedHistory, HttpStatus.OK.value()));
    }

    // ✅ Soft Delete an E-Summon History entry
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> deleteESummonHistory(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new GenericResponse<>(false,
                    "ID cannot be null", null, HttpStatus.BAD_REQUEST.value()));
        }

        eSummonHistoryService.deleteESummonHistory(id);
        return ResponseEntity.ok(new GenericResponse<>(true, "E-Summon history deleted successfully", null, HttpStatus.OK.value()));
    }

    // new Code

//    // Get ESummonHistory by ID for sumon and esumon all data
//    @GetMapping("/{id}")
//    public ResponseEntity<GenericResponse<ESummonHistoryDTO>> getById(@PathVariable Long id) {
//        // Fetch the E-Summon history by ID using the service
//        Optional<ESummonHistoryDTO> eSummonHistoryDTO = eSummonHistoryService.findById(id);
//
//        // Check if the data is present and return the response accordingly
//        if (eSummonHistoryDTO.isPresent()) {
//            GenericResponse<ESummonHistoryDTO> response = new GenericResponse<>(
//                    true, "Data retrieved successfully", eSummonHistoryDTO.get(), HttpStatus.OK.value());
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } else {
//            GenericResponse<ESummonHistoryDTO> response = new GenericResponse<>(
//                    false, "Data not found", null, HttpStatus.NOT_FOUND.value());
//            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//        }
//    }


    @PostMapping("/sending-esummon")
    public ResponseEntity<?> sendESummon(@RequestBody SendESummonRequest request) {
        try {
            List<ESummonHistoryDTO> historyDTOs = eSummonHistoryService.sendESummonAndUpdateHistory(
                    request.getId(), request.getMembers()
            );
            GenericResponse<List<ESummonHistoryDTO>> response = new GenericResponse<>(
                    true,
                    "eSummon sent successfully.",
                    historyDTOs,
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


    // get by sumon id
    @GetMapping("/by-esummon/{eSummonId}")
    public ResponseEntity<?> getHistoriesByESummonId(@PathVariable Long eSummonId) {
        List<ESummonHistoryDTO> dtos = eSummonHistoryService.getHistoriesByESummonId(eSummonId);

        GenericResponse<List<ESummonHistoryDTO>> response = new GenericResponse<>(
                true,
                "Histories fetched successfully.",
                dtos,
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
    }


