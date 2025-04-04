package com.dsp.agendaPapers.eSummonNotification.repository;

import com.dsp.agendaPapers.eSummonNotification.entityrecord.ESummonMembers;
import com.dsp.agendaPapers.eSummonNotification.entityrecord.ESummonMembersId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ESummonMembersRepository extends JpaRepository<ESummonMembers, ESummonMembersId> {}
