package com.dsp.agendaPapers.prorogationnotice.service;

import com.dsp.agendaPapers.prorogationnotice.Dto.ProrogationNoticeDTO;

public interface ProrogationNoticeService {

	ProrogationNoticeDTO saveProrogationNotice(ProrogationNoticeDTO prorogationNoticeDTO);

	ProrogationNoticeDTO getPropagationNoticeById(Long id);



}
