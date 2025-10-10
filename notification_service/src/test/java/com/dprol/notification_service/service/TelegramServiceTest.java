package com.dprol.notification_service.service;

import com.dprol.notification_service.consumer.UpdateConsumer;
import com.dprol.notification_service.dto.UserDto;
import com.dprol.notification_service.entity.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TelegramServiceTest {

    @Mock
    private UpdateConsumer updateConsumer;

    @InjectMocks
    private TelegramService telegramService;

    private UserDto user;

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setId(123L);
        user.setUsername("daniel");
    }

    @Test
    void sendNotification_ShouldCallUpdateConsumer() {
        String message = "Hello, Telegram!";

        telegramService.sendNotification(user, message);

        // Проверяем, что updateConsumer вызван с правильными аргументами
               verify(updateConsumer, times(1)).sendMessage(123L, message);
    }

    @Test
    void getContacts_ShouldReturnTelegram() {
        Contact contact = telegramService.getContacts();

        assertEquals(Contact.TELEGRAM, contact);
    }
}
