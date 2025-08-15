package com.example.customer.service.impl;

import com.example.customer.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisNotificationServiceImpl implements NotificationService {
    private static final String NOTIFICATION_KEY = "login:attempt:%s";
    private static final String LOGIN_BLOCKED_KEY = "login:blocked:%s";
    private final RedisTemplate<String, Object> redisTemplate;
    private static final int MAX_ATTEMPTS = 5;
    private static final int ATTEMPT_WINDOW_MINUTES = 5;
    private static final int BLOCK_DURATION_MINUTES = 15;

    /**
     * Login urinishini yozib qo'yadi
     *
     * @param identifier - foydalanuvchi nomi, email yoki telefon raqami
     * @return qolgan urinishlar soni
     */
    @Override
    public int recordLoginAttempt(String identifier) {

        var key = String.format(NOTIFICATION_KEY, identifier);
        var attempts = redisTemplate.opsForValue().increment(key);

        if (attempts != null && attempts == 1) {
            redisTemplate.expire(key, ATTEMPT_WINDOW_MINUTES, TimeUnit.MINUTES);
        }
        if (attempts != null && attempts >= MAX_ATTEMPTS) {
            blockUser(identifier);
        }

        return attempts != null ? MAX_ATTEMPTS - attempts.intValue() : MAX_ATTEMPTS;
    }

    @Override
    public void blockUser(String identifier) {
        var key = String.format(LOGIN_BLOCKED_KEY, identifier);
        redisTemplate.opsForValue().set(key, "blocked", BLOCK_DURATION_MINUTES, TimeUnit.MINUTES);

        var attemptKey = String.format(NOTIFICATION_KEY, identifier);
        redisTemplate.delete(attemptKey);

    }

    @Override
    public boolean isBlocked(String identifier) {
        var key = String.format(NOTIFICATION_KEY, identifier);
        return !identifier.equals("UNKNOWN") && redisTemplate.hasKey(key);
    }


    @Override
    public void loginSuccess(String identifier) {
        var attemptKey = String.format(NOTIFICATION_KEY, identifier);
        var blockedKey = String.format(LOGIN_BLOCKED_KEY, identifier);

        redisTemplate.delete(attemptKey);
        redisTemplate.delete(blockedKey);
    }

    @Override
    public Integer getRemainingAttempts(String identifier) {
        var key = String.format(NOTIFICATION_KEY, identifier);
        var attempts = (Long) redisTemplate.opsForValue().get(key);
        return attempts != null ? MAX_ATTEMPTS - attempts.intValue() : MAX_ATTEMPTS;

    }
}
