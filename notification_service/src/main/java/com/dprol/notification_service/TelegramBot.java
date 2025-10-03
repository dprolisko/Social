package com.dprol.notification_service;

import com.dprol.notification_service.consumer.UpdateConsumer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component

public class TelegramBot implements SpringLongPollingBot {

    private final UpdateConsumer updateConsumer;

    public TelegramBot(UpdateConsumer updateConsumer) {
        this.updateConsumer = updateConsumer;
    }

    @Override
    public String getBotToken() {
        return "7926081924:AAF1YkPtHrTPEJ-Cts_Af8QxWfRliwfLWzk";
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}
