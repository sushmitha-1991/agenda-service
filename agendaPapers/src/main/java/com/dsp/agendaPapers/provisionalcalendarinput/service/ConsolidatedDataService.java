//package com.dsp.agendaPapers.provisionalcalendarinput.service;
//
//import com.dsp.agendaPapers.entity.ApplicationInstance;
//import com.dsp.agendaPapers.pcquestionbranchinput.entity.QuestionbranchInput;
//import com.dsp.agendaPapers.pcquestionbranchinput.repository.QuestionbranchInputRepository;
//import com.dsp.agendaPapers.provisionalcalendarinput.Dto.ConsolidatedDataDTO;
//import com.dsp.agendaPapers.provisionalcalendarinput.entity.ProvisionalCalendarInputTest;
//import com.dsp.agendaPapers.provisionalcalendarinput.repository.ProvisionalCalendarInputRepository;
//import com.dsp.agendaPapers.repository.ApplicationInstanceRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class ConsolidatedDataService {
//
//    @Autowired
//    private ProvisionalCalendarInputRepository provisionalCalendarRepository;
//
//    @Autowired
//    private QuestionbranchInputRepository questionbranchRepository;
//
//    @Autowired
//    private ApplicationInstanceRepository applicationInstanceRepository;
//
//    public List<ConsolidatedDataDTO> getAllConsolidatedData() {
//        List<ConsolidatedDataDTO> consolidatedDataList = new ArrayList<>();
//
//        // Fetch all data from the three tables
//        List<ProvisionalCalendarInputTest> provisionalCalendarData = provisionalCalendarRepository.findAll();
//        List<QuestionbranchInput> questionbranchData = questionbranchRepository.findAll();
//        List<ApplicationInstance> applicationData = applicationInstanceRepository.findAll();
//
//        // Process data and map to DTO
//        for (ProvisionalCalendarInputTest provisionalData : provisionalCalendarData) {
//            ConsolidatedDataDTO dto = new ConsolidatedDataDTO();
//
//            // Map ProvisionalCalendarInputTest data
//            dto.setDateOfInput(provisionalData.getDateOfInput());
//            dto.setLanguage(provisionalData.getLanguage());
//            dto.setSubmittedBy(provisionalData.getSubmittedBy());
//            dto.setSubject(provisionalData.getSubject());
//            dto.setDateOfSittingInputs(provisionalData.getDateOfSittingInputs());
//
//
//
//            // Optionally map QuestionbranchInput data
//            if (!questionbranchData.isEmpty()) {
//                QuestionbranchInput questionData = questionbranchData.get(0); // Assuming 1:1 or other matching logic
////                dto.setStatusTableOffice(questionData.getStatus());
//                dto.setGroupchartsLS(questionData.getGroupchartsLS());
//            }
//
//            // Optionally map ApplicationInstance data
//            if (!applicationData.isEmpty()) {
//                ApplicationInstance appInstance = applicationData.get(0); // Assuming 1:1 or other matching logic
//                dto.setId(appInstance.getId());
//                dto.setApplicationNo(appInstance.getApplicationNo());
//                dto.setCurrentStatus(appInstance.getCurrentStatus());
//                dto.setPublishedDocumentUrl(appInstance.getPublishedDocumentUrl());
//                dto.setId(appInstance.getId());
//
//            }
//
//            // Add to result list
//            consolidatedDataList.add(dto);
//        }
//
//        return consolidatedDataList;
//    }
//}
//
