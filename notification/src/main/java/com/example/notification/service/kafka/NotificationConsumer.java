package com.example.notification.service.kafka;


import com.example.notification.dto.kafka.EmailHistory;
import com.example.notification.dto.kafka.SmsHistory;
import com.example.notification.entity.NotificationEntity;
import com.example.notification.enums.NotificationType;
import com.example.notification.repository.NotificationRepository;
import com.example.notification.service.MailSenderService;
import com.example.notification.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;
    private final MailSenderService mailSenderService;

    @KafkaListener(topics = "${kafka-topics.producer.topic.email}")
    public void sedEmailHistory(ConsumerRecord<String, String> record) {
        var key = record.key();
        var value = record.value();

        var data = Utils.parseObject(value, EmailHistory.class);

        notificationRepository.save(NotificationEntity.builder()
                .key(key)
                .emailHistory(data)
                .notificationType(NotificationType.EMAIL)
                .build());
        mailSenderService.sendVerificationEmail(data.email(), data.name(), data.jwt());
    }

    @KafkaListener(topics = "${kafka-topics.producer.topic.sms}")
    public void sendSmsNotification(ConsumerRecord<String, String> record) {
        var key = record.key();
        var value = record.value();

        var data = Utils.parseObject(value, new ParameterizedTypeReference<SmsHistory>() {
        });
        notificationRepository.save(NotificationEntity.builder()
                .key(key)
                .smsHistory(data)
                .notificationType(NotificationType.SMS)
                .build());
    }


}
