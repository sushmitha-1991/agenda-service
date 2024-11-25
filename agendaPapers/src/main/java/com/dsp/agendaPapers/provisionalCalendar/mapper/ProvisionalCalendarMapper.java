    package com.dsp.agendaPapers.provisionalCalendar.mapper;


    import com.dsp.agendaPapers.provisionalCalendar.Dto.ProvisionalCalendarDTO;
    import com.dsp.agendaPapers.provisionalCalendar.entity.ProvisionalCalendar;

    import java.util.List;
    import java.util.Map;

    public class ProvisionalCalendarMapper {
        public static ProvisionalCalendar toEntity(ProvisionalCalendarDTO provisionalCalendarDTO) {
            return ProvisionalCalendar.builder()
                    .id(provisionalCalendarDTO.getId())
                    .lokSabhaNo(provisionalCalendarDTO.getLokSabhaNo())
                    .sessionNo(provisionalCalendarDTO.getSessionNo())
                    .rajyaSabhaSessionNo(provisionalCalendarDTO.getRajyaSabhaSessionNo())
                    .content(provisionalCalendarDTO.getContent())
                    .generatedDocId(provisionalCalendarDTO.getGeneratedDocId())
                    .draftStatus(provisionalCalendarDTO.getDraftStatus())
                    .applicationNo(provisionalCalendarDTO.getApplicationNo())
                    .refId(provisionalCalendarDTO.getRefId())
                    .currentStatus(provisionalCalendarDTO.getCurrentStatus())
                    .publishedDocumentUrl(provisionalCalendarDTO.getPublishedDocumentUrl())
                    .processInstanceId(provisionalCalendarDTO.getProcessInstanceId())
                    .taskId(provisionalCalendarDTO.getTaskId())
                    .isPublished(provisionalCalendarDTO.getIsPublished())
                    .lokSabhaSessionNumber(provisionalCalendarDTO.getLokSabhaSessionNumber())
                    .lokSabhaSession(provisionalCalendarDTO.getLokSabhaSession())
                    .rajyaSabhaSessionNo(provisionalCalendarDTO.getRajyaSabhaSessionNo())
                    .otherInfo((Map<String, Object>) provisionalCalendarDTO.getOtherInfo())
                    .build();
        }

        public static ProvisionalCalendarDTO toDTO(ProvisionalCalendar provisionalCalendar) {
            return ProvisionalCalendarDTO.builder()
                    .id(provisionalCalendar.getId())
                    .lokSabhaNo(provisionalCalendar.getLokSabhaNo())
                    .sessionNo(provisionalCalendar.getSessionNo())
                    .rajyaSabhaSessionNo(provisionalCalendar.getRajyaSabhaSessionNo())
                    .content(provisionalCalendar.getContent())
                    .generatedDocId(provisionalCalendar.getGeneratedDocId())
                    .draftStatus(provisionalCalendar.getDraftStatus())
                    .applicationNo(provisionalCalendar.getApplicationNo())
                    .refId(provisionalCalendar.getRefId())
                    .currentStatus(provisionalCalendar.getCurrentStatus())
                    .publishedDocumentUrl(provisionalCalendar.getPublishedDocumentUrl())
                    .processInstanceId(provisionalCalendar.getProcessInstanceId())
                    .taskId(provisionalCalendar.getTaskId())
                    .isPublished(provisionalCalendar.getIsPublished())
                    .lokSabhaSessionNumber(provisionalCalendar.getLokSabhaSessionNumber())
                    .lokSabhaSession(provisionalCalendar.getLokSabhaSession())
                    .rajyaSabhaSession(provisionalCalendar.getRajyaSabhaSession())
                    .otherInfo((List<Map<String, Object>>) provisionalCalendar.getOtherInfo())

                    .build();
        }
    }
