package com.example.customer.service.kafka;

import com.example.customer.dto.request.KafkaSendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.example.customer.util.KafkaUtilKey.generateScoringKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {
    @Value("${kafka-topics.producer.topic}")
    private String topicName;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEmail(KafkaSendMessage kafkaSendMessage) {
        var key = generateScoringKey(kafkaSendMessage.email());
        ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, key, kafkaSendMessage);

        kafkaTemplate.send(record).whenComplete((records, e) -> {
            if (e != null) {
                log.error("Error send email {} massage {}", kafkaSendMessage.email(), e.getMessage());
            } else {
                log.debug("Message  sent. email: {}, ", kafkaSendMessage.email());
            }
        });

    }

    public void sendPhone(String phone) {
        var key = generateScoringKey(phone);
        ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, key, phone);

        kafkaTemplate.send(record).whenComplete((records, e) -> {
            if (e != null) {
                log.error("Error send phone {} massage {}", phone, e.getMessage());
            } else {
                log.debug("Message  sent. phone: {}, ", phone);
            }
        });

    }
}
