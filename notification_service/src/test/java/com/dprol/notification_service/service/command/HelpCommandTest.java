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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelpCommandTest {

    @Mock
    private TelegramProfileService profileService; // хотя в этом классе он не используется

    @Mock
    private CommandBuilder commandBuilder;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private HelpCommand helpCommand;


    @Test
    void sendMessage_ShouldReturnSendMessage() {
        Long chatId = 123L;
        String username = "daniel";
        String expectedText = "Help message text";

        SendMessage expectedSendMessage = new SendMessage(chatId.toString(), expectedText);

        when(messageSource.getMessage(eq("telegram.help"), isNull(), eq(Locale.getDefault())))
                .thenReturn(expectedText);
        when(commandBuilder.buildCommand(chatId, expectedText)).thenReturn(expectedSendMessage);

        SendMessage result = helpCommand.sendMessage(chatId, username);

        // Проверяем, что вызваны зависимости
        verify(messageSource).getMessage("telegram.help", null, Locale.getDefault());
        verify(commandBuilder).buildCommand(chatId, expectedText);

        // Проверяем результат
        assertEquals(expectedSendMessage, result);
        assertEquals(chatId.toString(), result.getChatId());
        assertEquals(expectedText, result.getText());
    }
}
