package com.dsp.agendaPapers.eSummonNotification.repository;



import com.dsp.agendaPapers.eSummonNotification.entity.PersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long> {

    // new code for mail
    @Query("SELECT e.memberId FROM ESummonMembers e WHERE e.eSummonId = :eSummonId")
    List<Long> findMemberIdsByESummonId(@Param("eSummonId") Long eSummonId);

}

