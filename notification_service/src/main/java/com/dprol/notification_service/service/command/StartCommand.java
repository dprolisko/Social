package com.dprol.notification_service.service.command;

import com.dprol.notification_service.entity.TelegramProfile;
import com.dprol.notification_service.service.TelegramProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;
import java.util.Optional;

@Component(value = "/start")
@RequiredArgsConstructor

public class StartCommand implements Command{

    private final TelegramProfileService profileService;

    private final CommandBuilder commandBuilder;

    private final MessageSource messageSource;

    @Override
    public SendMessage sendMessage(Long chatId, String username) {
        Optional<TelegramProfile> telegramProfile = profileService.findByChatId(chatId);
        if (telegramProfile.isEmpty()) {
            String message1 = messageSource.getMessage("telegram.not_registered", null, Locale.getDefault());
            return commandBuilder.buildCommand(chatId, message1);
        }
        TelegramProfile telegramProfile1 = telegramProfile.get();
        telegramProfile1.set_active(!telegramProfile1.is_active());
        profileService.saveTelegramProfile(telegramProfile1);
        String code = telegramProfile1.is_active() ? "telegram.start" : "telegram.stop";
        String message2 = messageSource.getMessage(code, new String[] {username}, Locale.getDefault());
        return commandBuilder.buildCommand(chatId, message2);
    }
}
