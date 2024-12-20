//package com.dsp.agendaPapers.prorogationnotice.service;
//
//import com.dsp.agendaPapers.prorogationnotice.Dto.ProrogationNoticeDTO;
//import com.dsp.agendaPapers.prorogationnotice.entity.ProrogationNotice;
//import com.dsp.agendaPapers.prorogationnotice.repository.ProrogationNoticeRepository;
//import com.dsp.agendaPapers.response.GenericResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//public class ProrogationNoticeServiceImpl implements ProrogationNoticeService{
//
//    @Autowired
//    private ProrogationNoticeRepository prorogationNoticeRepository;
//
//
//    @Override
//    public ProrogationNoticeDTO createProrogationNotice(ProrogationNoticeDTO prorogationNoticeDTO) {
//
//        ProrogationNotice  prorogationNotice = ProrogationNotice.builder()
//
//
//                .build();
//        return null;
//    }
//
//    @Override
//    public ProrogationNoticeDTO updateProrogationNotice(Long id, ProrogationNoticeDTO prorogationNoticeDTO) {
//        return null;
//    }
//
//    @Override
//    public ProrogationNoticeDTO getProrogationNoticeById(Long id) {
//        return null;
//    }
//
//    @Override
//    public List<ProrogationNoticeDTO> getAllProrogationNotices() {
//        return List.of();
//    }
//
//    @Override
//    public void deleteProrogationNotice(Long id) {
//
//    }
//}