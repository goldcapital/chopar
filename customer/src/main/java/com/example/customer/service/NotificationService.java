package com.example.customer.service;

public interface NotificationService {
    int recordLoginAttempt(String identifier);

    void blockUser(String email);
    boolean isBlocked(String identifier);
    void  loginSuccess(String identifier);
    Integer getRemainingAttempts(String identifier);
}
