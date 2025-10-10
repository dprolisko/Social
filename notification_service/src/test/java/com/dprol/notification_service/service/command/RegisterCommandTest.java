package com.dprol.notification_service.service.command;

import com.dprol.notification_service.client.UserServiceClient;
import com.dprol.notification_service.dto.ContactDto;
import com.dprol.notification_service.entity.TelegramProfile;
import com.dprol.notification_service.service.TelegramProfileService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterCommandTest {

    @Mock
    private TelegramProfileService profileService;
    @Mock
    private CommandBuilder commandBuilder;
    @Mock
    private MessageSource messageSource;
    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private RegisterCommand registerCommand;

    private final Long chatId = 123L;
    private final String username = "daniel";


    @Test
    void sendMessage_ShouldReturnAlreadyRegistered_WhenProfileExists() {
        String expectedMessage = "Already registered!";
        SendMessage expectedSendMessage = new SendMessage(chatId.toString(), expectedMessage);

        when(profileService.existsByChatId(chatId)).thenReturn(true);
        when(messageSource.getMessage("telegram.start.already_registered", null, Locale.getDefault()))
                .thenReturn(expectedMessage);
        when(commandBuilder.buildCommand(chatId, expectedMessage)).thenReturn(expectedSendMessage);

        SendMessage result = registerCommand.sendMessage(chatId, username);

        verify(profileService).existsByChatId(chatId);
        verify(commandBuilder).buildCommand(chatId, expectedMessage);
        assertEquals(expectedSendMessage, result);
    }

    @Test
    void sendMessage_ShouldRegisterNewUser_WhenNotExists() {
        String expectedMessage = "Registered successfully!";
        SendMessage expectedSendMessage = new SendMessage(chatId.toString(), expectedMessage);

        ContactDto contactDto = new ContactDto();
        contactDto.setUserId(999L);

        when(profileService.existsByChatId(chatId)).thenReturn(false);
        when(userServiceClient.getContactByUsername(username)).thenReturn(contactDto);
        when(messageSource.getMessage("telegram.start.registered", new String[]{username}, Locale.getDefault()))
                .thenReturn(expectedMessage);
        when(commandBuilder.buildCommand(chatId, expectedMessage)).thenReturn(expectedSendMessage);

        SendMessage result = registerCommand.sendMessage(chatId, username);

        verify(profileService).saveTelegramProfile(any(TelegramProfile.class));
        verify(commandBuilder).buildCommand(chatId, expectedMessage);
        assertEquals(expectedSendMessage, result);
    }

    @Test
    void sendMessage_ShouldReturnFailedMessage_WhenFeignExceptionThrown() {
        String expectedMessage = "Registration failed!";
        SendMessage expectedSendMessage = new SendMessage(chatId.toString(), expectedMessage);

        when(profileService.existsByChatId(chatId)).thenReturn(false);
        when(userServiceClient.getContactByUsername(username)).thenThrow(mock(FeignException.class));
        when(messageSource.getMessage("telegram.start.failed", null, Locale.getDefault()))
                .thenReturn(expectedMessage);
        when(commandBuilder.buildCommand(chatId, expectedMessage)).thenReturn(expectedSendMessage);

        SendMessage result = registerCommand.sendMessage(chatId, username);

        verify(commandBuilder).buildCommand(chatId, expectedMessage);
        assertEquals(expectedSendMessage, result);
    }
}
