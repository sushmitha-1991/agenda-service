package com.dsp.agendaPapers.prorogationnotice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsp.agendaPapers.prorogationnotice.entity.ProrogationNotice;

@Repository
public interface ProrogationNoticeRepository extends JpaRepository<ProrogationNotice, Long> {

	List<ProrogationNotice> findByLokSabhaSessionNo(String lokSabhaSessionNo);

}
