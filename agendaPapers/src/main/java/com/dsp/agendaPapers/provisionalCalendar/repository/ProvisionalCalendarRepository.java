//package com.dsp.agendaPapers.provisionalCalendar.repository;
//
//import com.dsp.agendaPapers.provisionalCalendar.entity.ProvisionalCalendar;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//
//@Repository
//public interface ProvisionalCalendarRepository  extends JpaRepository<ProvisionalCalendar, Long> {
//
//        // Find all records by Lok Sabha number
//        List<ProvisionalCalendar> findByLokSabhaNo(String lokSabhaNo);
//
//        // Find all records by Session number
//        List<ProvisionalCalendar> findBySessionNo(String sessionNo);
//
//        // Find all records by Rajya Sabha session number
//        List<ProvisionalCalendar> findByRajyaSabhaSessionNo(String rajyaSabhaSessionNo);
//
//        // Find all records where draft status matches the given value
//        List<ProvisionalCalendar> findByDraftStatus(Boolean draftStatus);
//
////        // Find records containing a specific document ID in the generatedDocId list
////        List<ProvisionalCalendar> findByGeneratedDocIdContains(Integer docId);
//
//        // Find all records with pagination and sorting by draft status
//        Page<ProvisionalCalendar> findByDraftStatus(Boolean draftStatus, Pageable pageable);
//
//        // Find all records by Lok Sabha number and Session number with pagination and sorting
//        Page<ProvisionalCalendar> findByLokSabhaNoAndSessionNo(String lokSabhaNo, String sessionNo, Pageable pageable);
//
//        // Find all records by Lok Sabha number, Session number, and Rajya Sabha session number with pagination and sorting
//        Page<ProvisionalCalendar> findByLokSabhaNoAndSessionNoAndRajyaSabhaSessionNo(String lokSabhaNo, String sessionNo, String rajyaSabhaSessionNo, Pageable pageable);
//    }
