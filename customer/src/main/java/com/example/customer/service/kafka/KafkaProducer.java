package com.example.customer.service.kafka;

import com.example.customer.dto.request.KafkaSendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.example.customer.util.KafkaUtilKey.generateScoringKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {
    @Value("${kafka-topics.producer.topic.sms}")
    private String topicSmsName;
    @Value("${kafka-topics.producer.topic.email}")
    private String topicEmailName;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEmail(KafkaSendMessage kafkaSendMessage) {
        var key = generateScoringKey(kafkaSendMessage.email());
        ProducerRecord<String, Object> record = buildRecord(topicEmailName, key, kafkaSendMessage, null);

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
        ProducerRecord<String, Object> record = buildRecord(topicSmsName, key, phone, null);

        kafkaTemplate.send(record).whenComplete((records, e) -> {
            if (e != null) {
                log.error("Error send phone {} massage {}", phone, e.getMessage());
            } else {
                log.debug("Message  sent. phone: {}, ", phone);
            }
        });

    }

    public <K, V> ProducerRecord<K, V> buildRecord(String topic, K key, V value, Map<String, String> headers) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
        if (headers != null) {
            headers.forEach((s, o) -> {
                record.headers().add(s, o.getBytes(StandardCharsets.UTF_8));
            });
        }
        return record;
    }
}
