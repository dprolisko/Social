package com.dprol.notification_service.service.command;

import com.dprol.notification_service.client.UserServiceClient;
import com.dprol.notification_service.dto.ContactDto;
import com.dprol.notification_service.entity.TelegramProfile;
import com.dprol.notification_service.service.TelegramProfileService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

@Component(value = "/register")
@RequiredArgsConstructor

public class RegisterCommand implements Command {

    private final TelegramProfileService profileService;

    private final CommandBuilder commandBuilder;

    private final MessageSource messageSource;

    private final UserServiceClient userServiceClient;

    @Override
    public SendMessage sendMessage(Long chatId, String username) {
        String message;
        if (profileService.existsByChatId(chatId)) {
            message = messageSource.getMessage("telegram.start.already_registered", null, Locale.getDefault());
            return commandBuilder.buildCommand(chatId, message);
        }
        try{
            ContactDto contactDto = userServiceClient.getContactByUsername(username);
            TelegramProfile telegramProfile = TelegramProfile.builder().telegramId(chatId).username(username).userId(contactDto.getUserId()).build();
            profileService.saveTelegramProfile(telegramProfile);
            message = messageSource.getMessage("telegram.start.registered", new String[] {username}, Locale.getDefault());
        }
        catch (FeignException e){
            message = messageSource.getMessage("telegram.start.failed", null, Locale.getDefault());
        }
        return commandBuilder.buildCommand(chatId, message);
    }
}
