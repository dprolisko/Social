package com.dprol.notification_service.service.command;

import com.dprol.notification_service.service.TelegramProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

@Component(value = "/help")
@RequiredArgsConstructor

public class HelpCommand implements Command{

    private final TelegramProfileService profileService;

    private final CommandBuilder commandBuilder;

    private final MessageSource messageSource;

    @Override
    public SendMessage sendMessage(Long chatId, String username) {
        String message = messageSource.getMessage("telegram.help", null, Locale.getDefault());
        return commandBuilder.buildCommand(chatId, message);
    }
}
