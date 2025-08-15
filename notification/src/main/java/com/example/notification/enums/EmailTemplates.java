package com.example.notification.enums;

import lombok.Getter;

public enum EmailTemplates {
    SEND_EMILE_VERIFICATION("verification.html","Complete Registration");
    @Getter
    private final String template;
    @Getter
    private final String subject;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
