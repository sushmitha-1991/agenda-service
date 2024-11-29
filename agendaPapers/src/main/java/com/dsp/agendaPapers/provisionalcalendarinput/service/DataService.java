//package com.dsp.agendaPapers.provisionalcalendarinput.service;
//
//import com.dsp.agendaPapers.pcquestionbranchinput.entity.QuestionbranchInput;
//import com.dsp.agendaPapers.pcquestionbranchinput.repository.QuestionbranchInputRepository;
//import com.dsp.agendaPapers.provisionalcalendarinput.entity.ProvisionalCalendarInputTest;
//import com.dsp.agendaPapers.provisionalcalendarinput.repository.ProvisionalCalendarInputRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class DataService {
//
//        private final ProvisionalCalendarInputRepository provisionalRepo;
//        private final QuestionbranchInputRepository questionbranchRepo;
//
//        public DataService(ProvisionalCalendarInputRepository provisionalRepo,
//                           QuestionbranchInputRepository questionbranchRepo) {
//            this.provisionalRepo = provisionalRepo;
//            this.questionbranchRepo = questionbranchRepo;
//        }
//
//        public Map<String, Object> getAllData() {
//            // Fetch all records from both tables
//            List<ProvisionalCalendarInputTest> provisionalData = provisionalRepo.findAll();
//            List<QuestionbranchInput> questionbranchData = questionbranchRepo.findAll();
//
//            // Prepare the response
//            Map<String, Object> response = new HashMap<>();
//            response.put("provisionalCalendarInputs", provisionalData);
//            response.put("questionbranchInputs", questionbranchData);
//
//            return response;
//        }
//    }
//
