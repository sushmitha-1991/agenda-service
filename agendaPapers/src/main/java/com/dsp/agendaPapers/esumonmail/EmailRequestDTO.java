package com.dsp.agendaPapers.esumonmail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDTO {
    private String filePath;
    private String emailFrom;
    private String emailSub;
    private String emailTo;
    private String emailBody;

    // Getters and Setters
}
