package com.dprol.notification_service.service;

import com.dprol.notification_service.dto.UserDto;
import com.dprol.notification_service.entity.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class EmailService implements NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Override
    public void sendNotification(UserDto user, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom(username);
        mailMessage.setSubject("Notification Service");
        mailMessage.setText(message);
        try{
            mailSender.send(mailMessage);
        }
        catch(Exception e){throw new RuntimeException("Failed to send notification");}
    }

    @Override
    public Contact getContacts() {
        return Contact.EMAIL;
    }
}
