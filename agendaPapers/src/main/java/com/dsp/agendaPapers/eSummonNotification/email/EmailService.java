package com.dsp.agendaPapers.eSummonNotification.email;

import com.dsp.agendaPapers.eSummonNotification.entity.ESummonHistory;

public interface EmailService {
    void sendEmail(ESummonHistory history, String unused1, String unused2, String token) throws Exception;


}
