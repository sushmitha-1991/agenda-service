package com.dsp.agendaPapers.provisionalcalendarinput.repository;

import com.dsp.agendaPapers.provisionalcalendarinput.Dto.ApplicationProvisionalQuestionDTO;
import com.dsp.agendaPapers.provisionalcalendarinput.entity.ProvisionalCalendarInputTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



import java.sql.Date;
import java.util.List;

//@EnableJpaRepositories
// extra I am adding the @EnableJPaRepo
@Repository
public interface ProvisionalCalendarInputRepository extends JpaRepository<ProvisionalCalendarInputTest,Long> {

    // where we can fetch the all the applicationinsatnce , questionbranch table , pc input

    @Query("SELECT new com.dsp.agendaPapers.provisionalcalendarinput.Dto.ApplicationProvisionalQuestionDTO(" +
            "ai.id, ai.applicationNo, ai.refId, ai.currentStatus, ai.publishedDocumentUrl, ai.processInstanceId, ai.taskId, ai.isPublished, " +
            "ai.lokSabhaSessionNumber, ai.lokSabhaSession, ai.rajyaSabhaSession, " +
            "pc.dateOfInput AS provisionalDateOfInput, pc.language, pc.submittedBy, pc.dateOfInput, pc.submittedByBranch, " +
            "pc.subject, pc.inputText, pc.footnoteText, pc.approverName, pc.approverDesignation, pc.dateOfSittingInputs, " +
            "pc.listOfDocument, pc.draftStatus, pc.content, pc.generatedFlag, " +
            "qb.groupchartsLS, qb.groupchartsRS, qb.dateOfInput AS questionBranchDateOfInput, qb.status AS questionBranchStatus, " +
            "qb.listOfDocumentTableOffice, qb.listOfDocumentTableOfficeFootnote, qb.listOfDocumentTableOfficeNote" +
            ") " +
            "FROM ApplicationInstance ai " +
            "LEFT JOIN ProvisionalCalendarInputTest pc ON pc.id = ai.id " +
            "LEFT JOIN QuestionbranchInput qb ON qb.refId = ai.id")

    List<ApplicationProvisionalQuestionDTO> fetchAllCombinedData();

}
