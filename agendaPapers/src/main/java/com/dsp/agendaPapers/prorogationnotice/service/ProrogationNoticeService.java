package com.dsp.agendaPapers.prorogationnotice.service;

import java.util.List;

import com.dsp.agendaPapers.prorogationnotice.Dto.ProrogationNoticeDTO;

public interface ProrogationNoticeService {

	ProrogationNoticeDTO saveProrogationNotice(ProrogationNoticeDTO prorogationNoticeDTO);

	ProrogationNoticeDTO getPropagationNoticeById(Long id);

	List<ProrogationNoticeDTO> getAllProrogationNoticeBySessionNo(String lokSabhaSessionNo);

	ProrogationNoticeDTO updateProrogationNotice(Long id, ProrogationNoticeDTO prorogationNoticeDTO);



}
