package com.example.notification.entity;

import com.example.notification.enums.NotificationType;
import com.example.notification.dto.kafka.EmailHistory;
import com.example.notification.dto.kafka.SmsHistory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NotificationEntity extends BastEntity {
    @Embedded
    private EmailHistory emailHistory;
    private String key;
    @Embedded
    private SmsHistory smsHistory;
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private NotificationType notificationType;

}
