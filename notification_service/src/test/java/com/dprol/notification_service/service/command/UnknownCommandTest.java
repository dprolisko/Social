package com.dprol.notification_service.service.command;

import com.dprol.notification_service.service.TelegramProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnknownCommandTest {

    @Mock
    private TelegramProfileService profileService; // хотя в UnknownCommand он не используется

    @Mock
    private CommandBuilder commandBuilder;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private UnknownCommand unknownCommand;

    private final Long chatId = 123L;
    private final String username = "daniel";


    @Test
    void sendMessage_ShouldReturnSendMessage() {
        String expectedText = "Unknown command!";
        SendMessage expectedSendMessage = new SendMessage(chatId.toString(), expectedText);

        when(messageSource.getMessage("telegram.unknown", null, Locale.getDefault()))
                .thenReturn(expectedText);
        when(commandBuilder.buildCommand(chatId, expectedText)).thenReturn(expectedSendMessage);

        SendMessage result = unknownCommand.sendMessage(chatId, username);

        verify(messageSource).getMessage("telegram.unknown", null, Locale.getDefault());
        verify(commandBuilder).buildCommand(chatId, expectedText);

        assertEquals(expectedSendMessage, result);
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(expectedText, result.getText());
    }
}
