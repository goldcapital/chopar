package com.example.customer.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KafkaUtilKey {
    public static String generateScoringKey(String emailOrPhone) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS");
        return emailOrPhone + "-" + LocalDateTime.now().format(formatter);
    }
}
