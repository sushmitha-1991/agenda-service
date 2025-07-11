package com.dsp.agendaPapers.eSummonNotification.dto;

import java.util.List;

import lombok.Data;

@Data
public class SummonResponseRSDTO {

	
	    private String sessionNoHin;
	    private String sessionNoEng;
	    private String summonDateEng;
	    private String summonDateTextEng;
	    private String textEnglish;
	    private String textHindi;
	    private String sgNameEng;
	    private String sgNameHin;
	    private String mpFullNameEng;
	    private String mpFullNameHin;
	    private String summonDateTextHin;
	    private List<FileDTO> files;

	    @Data
	    public static class FileDTO {
	        private String title;
	        private String url;
	    }
}
