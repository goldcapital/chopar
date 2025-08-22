package com.example.notification.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

import static com.example.notification.enums.EmailTemplates.SEND_EMILE_VERIFICATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String fromAccount;

    public void sendEmail(String toAccount, String subject, String text) {//to Account mana shu akovuntga -
        // subject habarni nomi buladi masalan registirishen
        // textda ichidagi kota texskar buladi
     /*   SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toAccount);
        msg.setFrom(fromAccount);
        msg.setSubject(subject);
        msg.setText(text);
        javaMailSender.send(msg);*/
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom(fromAccount);

            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(msg);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationEmail(String email, String name, String jwt) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            log.warn("SEND Email verification email {}", email);
            helper.setFrom(fromAccount);
            final var templateName = SEND_EMILE_VERIFICATION.getTemplate();
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", name);
            variables.put("verificationLink", "http://localhost:2222/auth/verification/email/" + jwt);

            Context context = new Context();
            context.setVariables(variables);
            var htmlContent = templateEngine.process(templateName, context);

            helper.setTo(email);
            helper.setSubject(SEND_EMILE_VERIFICATION.getSubject());
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
