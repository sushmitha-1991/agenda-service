    package com.dsp.agendaPapers.provisionalCalendar.mapper;


    import com.dsp.agendaPapers.provisionalCalendar.Dto.ProvisionalCalendarDTO;
    import com.dsp.agendaPapers.provisionalCalendar.entity.ProvisionalCalendar;

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
                    .build();
        }
    }
