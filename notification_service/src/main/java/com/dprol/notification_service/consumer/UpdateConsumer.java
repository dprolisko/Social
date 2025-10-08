package com.dprol.notification_service.consumer;

import com.dprol.notification_service.dto.TelegramBotProperty;
import com.dprol.notification_service.entity.TelegramProfile;
import com.dprol.notification_service.service.TelegramProfileService;
import com.dprol.notification_service.service.command.CommandBuilder;
import com.dprol.notification_service.service.command.CommandExcecuter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor

public class UpdateConsumer extends TelegramLongPollingBot {

    private final CommandExcecuter commandExcecuter;

    private final CommandBuilder commandBuilder;

    private final TelegramProfileService telegramProfileService;

    private final TelegramBotProperty telegramBotProperty;

    @PostConstruct
    public void botInit(){
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return telegramBotProperty.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();
            SendMessage sendMessage = commandExcecuter.commandExcecuter(chatId, username, message);
            executeMessage(sendMessage);
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotProperty.getName();
    }

    public void sendMessage(Long userId, String message){
        TelegramProfile telegramProfile = telegramProfileService.findByUserId(userId);
        if (telegramProfile.is_active()) {
            SendMessage sendMessage = commandBuilder.buildCommand(telegramProfile.getTelegramId(), message);
            executeMessage(sendMessage);
        }
        else {

        }
    }

    private void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}