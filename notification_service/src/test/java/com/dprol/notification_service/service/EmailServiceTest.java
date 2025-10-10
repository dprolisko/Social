package com.dprol.notification_service.service;

import com.dprol.notification_service.dto.UserDto;
import com.dprol.notification_service.entity.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private UserDto user;

    private UserDto user2;

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("test");
    }

    @Test
    void sendNotification_ShouldSendEmail() {
        // act
        emailService.sendNotification(user, "Hello, world!");

        // assert
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertArrayEquals(new String[]{"test@example.com"}, sentMessage.getTo());
        assertEquals("Notification Service", sentMessage.getSubject());
        assertEquals("Hello, world!", sentMessage.getText());
    }

    @Test
    void sendNotification_ShouldThrowRuntimeException_WhenMailSenderFails() {
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emailService.sendNotification(user, "Hello"));

        assertEquals("Failed to send notification", ex.getMessage());
    }

    @Test
    void getContacts_ShouldReturnEmail() {
        assertEquals(Contact.EMAIL, emailService.getContacts());
    }
}
