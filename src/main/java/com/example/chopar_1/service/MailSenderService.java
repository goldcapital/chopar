package com.example.chopar_1.service;



import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;
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
            MimeMessage msg=javaMailSender.createMimeMessage();
            msg.setFrom(fromAccount);

            MimeMessageHelper helper=new MimeMessageHelper(msg,true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(text,true);
            javaMailSender.send(msg);

        }catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }

}
