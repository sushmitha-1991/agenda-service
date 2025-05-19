package com.dsp.agendaPapers.eSummonNotification.event;

import com.dsp.agendaPapers.eSummonNotification.email.EmailService;
import com.dsp.agendaPapers.eSummonNotification.email.NumberConverterUtil;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonHistory;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonHistoryRepository;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonNotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ESummonNotificationConsumer {

    @Autowired
    private ESummonHistoryRepository historyRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ESummonNotificationRepository notificationRepository;

    @KafkaListener(topics = "${kafka.topic.eSummon}", groupId = "eSummonGroup")
    public void consume(String eSummonId) {
        log.info("Received Kafka event to send emails for eSummonId: {}", eSummonId);

        Long summonId = Long.parseLong(eSummonId);

        Optional<ESummonNotification> notificationOpt = notificationRepository.findById(summonId);
        if (notificationOpt.isEmpty()) {
            log.error("No eSummonNotification found for ID: {}", summonId);
            return;
        }

        ESummonNotification notification = notificationOpt.get();

        int sessionNo = NumberConverterUtil.parseIntSafe(notification.getSessionNo());
        int lokSabhaNo = NumberConverterUtil.parseIntSafe(notification.getLokSabhaNo());

        String sessionRoman = NumberConverterUtil.toRoman(sessionNo);
        String lokSabhaNoWord = NumberConverterUtil.toOrdinalWord(lokSabhaNo);

        List<ESummonHistory> pendingEntries =
                historyRepository.findByESummonIdAndMailStatus(summonId, "PENDING");

        for (ESummonHistory history : pendingEntries) {
            try {
                emailService.sendEmail(history, sessionRoman, lokSabhaNoWord);
                history.setMailStatus("SENT");
                history.setRetryCount(history.getRetryCount() + 1);
                historyRepository.save(history);
                log.info("Email sent to member: {} and status updated to SENT", history.getEmailId());
            } catch (Exception e) {
                log.error("Failed to send email to member: {}. Error: {}", history.getEmailId(), e.getMessage());
                history.setRetryCount(history.getRetryCount() + 1);
                historyRepository.save(history);
            }
        }
    }
}
