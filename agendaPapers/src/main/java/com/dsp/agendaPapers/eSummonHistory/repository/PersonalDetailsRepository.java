package com.dsp.agendaPapers.eSummonHistory.repository;



import com.dsp.agendaPapers.eSummonHistory.entity.PersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long> {
}

