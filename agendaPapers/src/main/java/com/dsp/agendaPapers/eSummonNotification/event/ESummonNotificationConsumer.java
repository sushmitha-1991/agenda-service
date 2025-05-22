package com.dsp.agendaPapers.eSummonNotification.event;

import com.dsp.agendaPapers.eSummonNotification.email.EmailService;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonHistory;
import com.dsp.agendaPapers.eSummonNotification.entity.ESummonNotification;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonHistoryRepository;
import com.dsp.agendaPapers.eSummonNotification.repository.ESummonNotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void consume(String eSummonIdStr) {
        log.info("Received Kafka event with eSummonId: {}", eSummonIdStr);
System.out.println("#### Received Kafka event with eSummonId ");
        Long eSummonId;
        try {
            eSummonId = Long.parseLong(eSummonIdStr);
        } catch (NumberFormatException e) {
            log.error("Invalid eSummonId received: {}", eSummonIdStr);
            return;
        }

        Optional<ESummonNotification> notificationOpt = notificationRepository.findById(eSummonId);
        System.out.println("### notificationOpt - " + notificationOpt);
        if (notificationOpt.isEmpty()) {
            log.error("No eSummonNotification found for ID: {}", eSummonId);
            return;
        }

        ESummonNotification notification = notificationOpt.get();
        int sessionNo = parseIntSafe(notification.getSessionNo());
        int lokSabhaNo = parseIntSafe(notification.getLokSabhaNo());

        String sessionRoman = toRoman(sessionNo);
        String lokSabhaNoWord = toOrdinalWord(lokSabhaNo);

        List<ESummonHistory> pendingEntries =
                historyRepository.findByeSummon_IdAndMailStatus(eSummonId, "PENDING");
         //logger
        log.info("[Kafka-Consumer] Found {} pending eSummonHistory entries", pendingEntries.size());

        for (ESummonHistory history : pendingEntries) {
            try {
                //logger
                log.info("[Kafka-Consumer] Attempting to send email to {}", history.getEmailId());

                emailService.sendEmail(history, sessionRoman, lokSabhaNoWord);
                history.setMailStatus("SENT");
                history.setRetryCount(history.getRetryCount() + 1);
                historyRepository.save(history);
                log.info("Email sent to member: {} and status updated to SENT", history.getEmailId());
            } catch (Exception e) {
                log.error("Failed to send email to member: {}. Error: {}", history.getEmailId(), e.getMessage());
                history.setRetryCount(history.getRetryCount() + 1);
                historyRepository.save(history);
                // logger
                log.warn("[Kafka-Consumer] Retry count incremented and saved for: {}", history.getEmailId());
            }
        }
    }

    private String toRoman(int number) {
        String[] romans = {
            "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X",
            "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX"
        };
        return (number > 0 && number < romans.length) ? romans[number] : String.valueOf(number);
    }

    private String toOrdinalWord(int number) {
        String[] ordinals = {
            "", "First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh",
            "Eighth", "Ninth", "Tenth", "Eleventh", "Twelfth", "Thirteenth", "Fourteenth",
            "Fifteenth", "Sixteenth", "Seventeenth", "Eighteenth", "Nineteenth", "Twentieth"
        };
        return (number > 0 && number < ordinals.length) ? ordinals[number] : String.valueOf(number);
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Unable to parse number from '{}'", value);
            return 0;
        }
    }
}
