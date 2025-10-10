package com.dprol.notification_service.service.command;

import com.dprol.notification_service.entity.TelegramProfile;
import com.dprol.notification_service.service.TelegramProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {

    @Mock
    private TelegramProfileService profileService;
    @Mock
    private CommandBuilder commandBuilder;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private StartCommand startCommand;

    private final Long chatId = 123L;
    private final String username = "daniel";

    @Test
    void sendMessage_ShouldReturnNotRegistered_WhenProfileNotFound() {
        String expectedMessage = "Not registered!";
        SendMessage expectedSendMessage = new SendMessage(chatId.toString(), expectedMessage);

        when(profileService.findByChatId(chatId)).thenReturn(Optional.empty());
        when(messageSource.getMessage("telegram.not_registered", null, Locale.getDefault()))
                .thenReturn(expectedMessage);
        when(commandBuilder.buildCommand(chatId, expectedMessage)).thenReturn(expectedSendMessage);

        SendMessage result = startCommand.sendMessage(chatId, username);

        verify(profileService).findByChatId(chatId);
        verify(commandBuilder).buildCommand(chatId, expectedMessage);
        assertEquals(expectedSendMessage, result);
    }

    @Test
    void sendMessage_ShouldActivateProfile_WhenInactive() {
        TelegramProfile profile = new TelegramProfile();
        profile.setTelegramId(chatId);
        profile.setUsername(username);
        profile.set_active(false);

        String expectedMessage = "Started!";
        SendMessage expectedSendMessage = new SendMessage(chatId.toString(), expectedMessage);

        when(profileService.findByChatId(chatId)).thenReturn(Optional.of(profile));
        when(messageSource.getMessage("telegram.start", new String[]{username}, Locale.getDefault()))
                .thenReturn(expectedMessage);
        when(commandBuilder.buildCommand(chatId, expectedMessage)).thenReturn(expectedSendMessage);

        SendMessage result = startCommand.sendMessage(chatId, username);

        assertTrue(profile.is_active()); // профиль должен стать активным
        verify(profileService).saveTelegramProfile(profile);
        verify(commandBuilder).buildCommand(chatId, expectedMessage);
        assertEquals(expectedSendMessage, result);
    }

    @Test
    void sendMessage_ShouldDeactivateProfile_WhenActive() {
        TelegramProfile profile = new TelegramProfile();
        profile.setTelegramId(chatId);
        profile.setUsername(username);
        profile.set_active(true);

        String expectedMessage = "Stopped!";
        SendMessage expectedSendMessage = new SendMessage(chatId.toString(), expectedMessage);

        when(profileService.findByChatId(chatId)).thenReturn(Optional.of(profile));
        when(messageSource.getMessage("telegram.stop", new String[]{username}, Locale.getDefault()))
                .thenReturn(expectedMessage);
        when(commandBuilder.buildCommand(chatId, expectedMessage)).thenReturn(expectedSendMessage);

        SendMessage result = startCommand.sendMessage(chatId, username);

        assertFalse(profile.is_active()); // профиль должен стать неактивным
        verify(profileService).saveTelegramProfile(profile);
        verify(commandBuilder).buildCommand(chatId, expectedMessage);
        assertEquals(expectedSendMessage, result);
    }
}
