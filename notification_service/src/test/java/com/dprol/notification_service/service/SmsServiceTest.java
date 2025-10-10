package com.dprol.notification_service.service;

import com.dprol.notification_service.dto.UserDto;
import com.dprol.notification_service.entity.Contact;
import com.dprol.notification_service.exception.SmsSendingException;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.sms.messages.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private VonageClient vonageClient;
    @Mock
    private SmsClient smsClient;
    @Mock
    private SmsSubmissionResponse response;

    @InjectMocks
    private SmsService smsService;

    private UserDto user;

    @BeforeEach
    void setUp() {
        user = new UserDto();
        user.setId(1L);
        user.setPhone("1234567890");
        user.setUsername("username");

        when(vonageClient.getSmsClient()).thenReturn(smsClient);
    }

    @Test
    void sendNotification_ShouldNotThrow_WhenStatusOk() {
        SmsSubmissionResponseMessage message = mock(SmsSubmissionResponseMessage.class);
        when(message.getStatus()).thenReturn(MessageStatus.OK);
        when(response.getMessages()).thenReturn(Collections.singletonList(message));
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        assertDoesNotThrow(() -> smsService.sendNotification(user, "Hello"));
        verify(smsClient).submitMessage(any(TextMessage.class));
    }

    @Test
    void sendNotification_ShouldThrow_WhenStatusNotOk() {
        SmsSubmissionResponseMessage message = mock(SmsSubmissionResponseMessage.class);
        when(message.getStatus()).thenReturn(MessageStatus.INTERNAL_ERROR);
        when(message.getErrorText()).thenReturn("Some error");
        when(response.getMessages()).thenReturn(Collections.singletonList(message));
        when(smsClient.submitMessage(any(TextMessage.class))).thenReturn(response);

        SmsSendingException ex = assertThrows(SmsSendingException.class,
                () -> smsService.sendNotification(user, "Hello"));

        assertTrue(ex.getMessage().contains("SMS SEND ERROR: Some error"));
    }
}
