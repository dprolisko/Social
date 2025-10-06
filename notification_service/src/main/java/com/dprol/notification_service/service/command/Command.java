package com.dprol.notification_service.service.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component

public interface Command {

    SendMessage sendMessage(Long chatId, String username);
}
