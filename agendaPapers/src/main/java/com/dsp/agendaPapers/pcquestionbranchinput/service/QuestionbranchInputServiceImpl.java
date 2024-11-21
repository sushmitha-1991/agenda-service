package com.dsp.agendaPapers.pcquestionbranchinput.service;

import com.dsp.agendaPapers.pcquestionbranchinput.Dto.QuestionbranchInputDTO;

import java.util.List;


import com.dsp.agendaPapers.pcquestionbranchinput.entity.QuestionbranchInput;
import com.dsp.agendaPapers.pcquestionbranchinput.repository.QuestionbranchInputRepository;
import com.dsp.agendaPapers.repository.ApplicationInstanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class QuestionbranchInputServiceImpl implements QuestionbranchInputService {

    @Autowired
    private QuestionbranchInputRepository questionbranchInputRepository;

    @Autowired
    ApplicationInstanceRepository applicationInstanceRepository;

    @Transactional
    @Override
    public QuestionbranchInputDTO createQuestionbranchInput(QuestionbranchInputDTO questionbranchInputDTO) {
        QuestionbranchInput questionbranchInput = QuestionbranchInput.builder()
                .groupchartsLS(questionbranchInputDTO.getGroupchartsLS())
                .groupchartsRS(questionbranchInputDTO.getGroupchartsRS())
                .listOfDocumentTableOffice(questionbranchInputDTO.getListOfDocumentTableOffice())
                .listOfDocumentTableOfficeFootnote(questionbranchInputDTO.getListOfDocumentTableOfficeFootnote())
                .listOfDocumentTableOfficeNote(questionbranchInputDTO.getListOfDocumentTableOfficeNote())
                .dateOfSittingInputs(questionbranchInputDTO.getDateOfSittingInputs())
                .dateOfInput(questionbranchInputDTO.getDateOfInput())
                .draftStatus(questionbranchInputDTO.getDraftStatus())
                .generatedFlag(questionbranchInputDTO.getGeneratedFlag())
                .inputText(questionbranchInputDTO.getInputText())
                .status(questionbranchInputDTO.getStatus())
                .submittedByBranch(questionbranchInputDTO.getSubmittedByBranch())
                // application instance class
                .id(questionbranchInputDTO.getId())
                .applicationNo(questionbranchInputDTO.getApplicationNo())
                .refId(questionbranchInputDTO.getRefId())
                .currentStatus(questionbranchInputDTO.getCurrentStatus())
                .publishedDocumentUrl(questionbranchInputDTO.getPublishedDocumentUrl())
                .processInstanceId(questionbranchInputDTO.getProcessInstanceId())
                .taskId(questionbranchInputDTO.getTaskId())
                .isPublished(questionbranchInputDTO.getIsPublished())
                .lokSabhaSession(questionbranchInputDTO.getLokSabhaSession())
                .lokSabhaSessionNumber(questionbranchInputDTO.getLokSabhaSessionNumber())
                .rajyaSabhaSession(questionbranchInputDTO.getRajyaSabhaSession())
                .build();

        QuestionbranchInput savedEntity = questionbranchInputRepository.save(questionbranchInput);
        return mapToDto(savedEntity);
    }

    @Override
    public QuestionbranchInputDTO getQuestionbranchInputById(Long id) {
        QuestionbranchInput questionbranchInput = questionbranchInputRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuestionbranchInput not found with the given ID"));
        return mapToDto(questionbranchInput);
    }

    @Override
    public List<QuestionbranchInputDTO> getAllQuestionbranchInputs() {
        return questionbranchInputRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionbranchInputDTO updateQuestionbranchInput(Long id, QuestionbranchInputDTO questionbranchInputDTO) {
        return questionbranchInputRepository.findById(id)
                .map(existingEntity -> {
                    existingEntity.setGroupchartsLS(questionbranchInputDTO.getGroupchartsLS());
                    existingEntity.setGroupchartsRS(questionbranchInputDTO.getGroupchartsRS());
                    existingEntity.setListOfDocumentTableOffice(questionbranchInputDTO.getListOfDocumentTableOffice());
                    existingEntity.setListOfDocumentTableOfficeFootnote(questionbranchInputDTO.getListOfDocumentTableOfficeFootnote());
                    existingEntity.setListOfDocumentTableOfficeNote(questionbranchInputDTO.getListOfDocumentTableOfficeNote());
                    existingEntity.setDateOfSittingInputs(questionbranchInputDTO.getDateOfSittingInputs());
                    existingEntity.setDateOfInput(questionbranchInputDTO.getDateOfInput());
                    existingEntity.setDraftStatus(questionbranchInputDTO.getDraftStatus());
                    existingEntity.setGeneratedFlag(questionbranchInputDTO.getGeneratedFlag());
                    existingEntity.setInputText(questionbranchInputDTO.getInputText());
                    existingEntity.setStatus(questionbranchInputDTO.getStatus());
                    existingEntity.setSubmittedByBranch(questionbranchInputDTO.getSubmittedByBranch());

                    // for application instance
                    existingEntity.setId(questionbranchInputDTO.getId());
                    existingEntity.setApplicationNo(questionbranchInputDTO.getApplicationNo());
                    existingEntity.setRefId(questionbranchInputDTO.getRefId());
                    existingEntity.setCurrentStatus(questionbranchInputDTO.getCurrentStatus());
                    existingEntity.setPublishedDocumentUrl(questionbranchInputDTO.getPublishedDocumentUrl());
                    existingEntity.setProcessInstanceId(questionbranchInputDTO.getProcessInstanceId());
                    existingEntity.setTaskId(questionbranchInputDTO.getTaskId());
                    existingEntity.setIsPublished(questionbranchInputDTO.getIsPublished());
                    existingEntity.setLokSabhaSessionNumber(questionbranchInputDTO.getLokSabhaSessionNumber());
                    existingEntity.setLokSabhaSession(questionbranchInputDTO.getLokSabhaSession());
                    existingEntity.setRajyaSabhaSession(questionbranchInputDTO.getRajyaSabhaSession());


                    QuestionbranchInput updatedEntity = questionbranchInputRepository.save(existingEntity);
                    return mapToDto(updatedEntity);
                }).orElseThrow(() -> new RuntimeException("QuestionbranchInput not found with id " + id));
    }

    @Override
    public void deleteQuestionbranchInput(Long id) {
        if (questionbranchInputRepository.existsById(id)) {
            questionbranchInputRepository.deleteById(id);
        } else {
            throw new RuntimeException("QuestionbranchInput not found with id " + id);
        }
    }

    @Override
    public List<QuestionbranchInputDTO> getQuestionbranchInputsByGroupchartsLS(String groupchartsLS) {
        return questionbranchInputRepository.findByGroupchartsLS(groupchartsLS)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionbranchInputDTO> getQuestionbranchInputsByGroupchartsRS(String groupchartsRS) {
        return questionbranchInputRepository.findByGroupchartsRS(groupchartsRS)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private QuestionbranchInputDTO mapToDto(QuestionbranchInput questionbranchInput) {
        return QuestionbranchInputDTO.builder()
                .id(questionbranchInput.getId())
                .groupchartsLS(questionbranchInput.getGroupchartsLS())
                .groupchartsRS(questionbranchInput.getGroupchartsRS())
                .listOfDocumentTableOffice(questionbranchInput.getListOfDocumentTableOffice())
                .listOfDocumentTableOfficeFootnote(questionbranchInput.getListOfDocumentTableOfficeFootnote())
                .listOfDocumentTableOfficeNote(questionbranchInput.getListOfDocumentTableOfficeNote())
                .dateOfSittingInputs(questionbranchInput.getDateOfSittingInputs())
                .dateOfInput(questionbranchInput.getDateOfInput())
                .draftStatus(questionbranchInput.getDraftStatus())
                .generatedFlag(questionbranchInput.getGeneratedFlag())
                .inputText(questionbranchInput.getInputText())
                .status(questionbranchInput.getStatus())
                .submittedByBranch(questionbranchInput.getSubmittedByBranch())
                // for application instance
                .id(questionbranchInput.getId())
                .applicationNo(questionbranchInput.getApplicationNo())
                .refId(questionbranchInput.getRefId())
                .currentStatus(questionbranchInput.getCurrentStatus())
                .publishedDocumentUrl(questionbranchInput.getPublishedDocumentUrl())
                .processInstanceId(questionbranchInput.getProcessInstanceId())
                .taskId(questionbranchInput.getTaskId())
                .isPublished(questionbranchInput.getIsPublished())
                .lokSabhaSessionNumber(questionbranchInput.getLokSabhaSessionNumber())
                .lokSabhaSession(questionbranchInput.getLokSabhaSession())
                .rajyaSabhaSession(questionbranchInput.getRajyaSabhaSession())

                .build();
    }
}
