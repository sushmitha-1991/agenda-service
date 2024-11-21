package com.dsp.agendaPapers.pcquestionbranchinput.service;

import com.dsp.agendaPapers.pcquestionbranchinput.Dto.QuestionbranchInputDTO;

import java.util.List;

public interface QuestionbranchInputService {

    QuestionbranchInputDTO createQuestionbranchInput(QuestionbranchInputDTO questionbranchInputDTO);
    QuestionbranchInputDTO updateQuestionbranchInput(Long id, QuestionbranchInputDTO questionbranchInputDTO);
    QuestionbranchInputDTO getQuestionbranchInputById(Long id);
    List<QuestionbranchInputDTO> getAllQuestionbranchInputs();
    void deleteQuestionbranchInput(Long id);
    List<QuestionbranchInputDTO> getQuestionbranchInputsByGroupchartsLS(String groupchartsLS);
    List<QuestionbranchInputDTO> getQuestionbranchInputsByGroupchartsRS(String groupchartsRS);
}
