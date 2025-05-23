package com.dsp.agendaPapers.eSummonNotification.email;

import com.dsp.agendaPapers.eSummonNotification.entity.ESummonHistory;

public interface EmailService {
    void sendEmail(ESummonHistory history, String sessionRoman, String lokSabhaNoWord, String token) throws Exception;


}
