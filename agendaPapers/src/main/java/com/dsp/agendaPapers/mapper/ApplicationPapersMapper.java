package com.dsp.agendaPapers.mapper;

import org.springframework.stereotype.Component;

import com.dsp.agendaPapers.applicationPapers.dto.ApplicationPapersDTO;
import com.dsp.agendaPapers.applicationPapers.entity.ApplicationPapers;

@Component
public class ApplicationPapersMapper {

    public ApplicationPapers toEntity(ApplicationPapersDTO applicationPapersDTO) {
        return ApplicationPapers.builder()
                .id(applicationPapersDTO.getId())
                .applicationInstanceId(applicationPapersDTO.getApplicationInstanceId())
                .applicationId(applicationPapersDTO.getApplicationId())
                .templateId(applicationPapersDTO.getTemplateId())
                .templateHindiId(applicationPapersDTO.getTemplateHindiId())
                .templateContent(applicationPapersDTO.getTemplateContent())
                .templateContentHindi(applicationPapersDTO.getTemplateContentHindi())
                .build();
    }

    public ApplicationPapersDTO toDTO(ApplicationPapers applicationPapers) {
        return ApplicationPapersDTO.builder()
                .id(applicationPapers.getId())
                .applicationInstanceId(applicationPapers.getApplicationInstanceId())
                .applicationId(applicationPapers.getApplicationId())
                .templateId(applicationPapers.getTemplateId())
                .templateHindiId(applicationPapers.getTemplateHindiId())
                .templateContent(applicationPapers.getTemplateContent())
                .templateContentHindi(applicationPapers.getTemplateContentHindi())
                .build();
    }
}

