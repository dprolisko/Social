package com.dprol.notification_service.consumer;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component

public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    public UpdateConsumer() {
        this.telegramClient = new OkHttpTelegramClient("7926081924:AAF1YkPtHrTPEJ-Cts_Af8QxWfRliwfLWzk");
    }

    @SneakyThrows
    @Override
    public void consume(Update update) {
        System.out.printf(
                "Пришло сообщение %s от %s%n",
        update.getMessage().getText(),
        update.getMessage().getChatId()
        );

        var chatId = update.getMessage().getChatId();
            SendMessage message = SendMessage.builder().text("preved").chatId(chatId).build();

            telegramClient.execute(message);
    }
}
