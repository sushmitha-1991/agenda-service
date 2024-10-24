package com.dsp.agendaPapers.applicationPapers.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsp.agendaPapers.applicationPapers.dto.ApplicationPapersDTO;
import com.dsp.agendaPapers.applicationPapers.entity.ApplicationPapers;
import com.dsp.agendaPapers.applicationPapers.repository.ApplicationPapersRepository;
import com.dsp.agendaPapers.applicationPapers.service.ApplicationPapersService;
import com.dsp.agendaPapers.mapper.ApplicationPapersMapper;

@Service
public class ApplicationPapersServiceImpl implements ApplicationPapersService {
	
	@Autowired
    private ApplicationPapersRepository applicationPapersRepository;

    @Autowired
    private ApplicationPapersMapper applicationPapersMapper;

    public ApplicationPapersDTO saveApplicationPapers(ApplicationPapersDTO applicationPapersDTO) {
        ApplicationPapers applicationPapers = applicationPapersMapper.toEntity(applicationPapersDTO);
        ApplicationPapers savedApplicationPapers = applicationPapersRepository.save(applicationPapers);
        return applicationPapersMapper.toDTO(savedApplicationPapers);
    }

}
