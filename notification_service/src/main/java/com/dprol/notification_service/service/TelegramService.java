package com.dprol.notification_service.service;

import com.dprol.notification_service.consumer.UpdateConsumer;
import com.dprol.notification_service.dto.UserDto;
import com.dprol.notification_service.entity.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class TelegramService implements NotificationService {

    private final UpdateConsumer updateConsumer;

    @Override
    public void sendNotification(UserDto user, String message) {
        updateConsumer.sendMessage(user.getId(), message);
    }

    @Override
    public Contact getContacts() {
        return Contact.TELEGRAM;
    }
}
