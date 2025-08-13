// src/main/java/com/dsp/agendaPapers/web/dto/WorkflowHistoryDto.java
package com.dsp.agendaPapers.eSummonNotification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowHistoryDto {
    private LocalDateTime createdAt;          // null
    private LocalDateTime updatedAt;          // null
    private Long createdBy;                   // null
    private Long updatedBy;                   // null
    private Boolean isActive;                 // null
    private Boolean isDeleted;                // null
    private Long id;                          // null

    private Long processInstanceId;           // null
    private Long subModuleId;                 // null
    private Long applicationInstanceId;       // null

    private Long actionTakenBy;               // null
    private String actionTakenByName = "";    // ""
    private LocalDateTime actionTakenOn;      // <-- set from e-summon.createdAt

    private String actionTaken;               // null
    private String status = "";               // ""
    private String statusKey;                 // null

    private Long pendingWith;                 // null
    private String pendingWithName = "";      // ""

    private String note;                      // null
    private LocalDateTime pendingStartDate;   // null
    private String initalStatusKey;           // null

    private String currentTaskId;             // null
    private String nextTaskId;                // null
    private String actionUrl;                 // null

    private Boolean draftDocument;            // null
    private Boolean translationSuccess;       // null
    private Boolean correctedDebateSuccess;   // null
}
