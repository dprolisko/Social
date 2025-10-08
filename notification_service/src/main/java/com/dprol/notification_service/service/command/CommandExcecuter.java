package com.dprol.notification_service.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;

@Component
@RequiredArgsConstructor

public class CommandExcecuter {

    private final Map<String, Command> commands;

    public SendMessage commandExcecuter(Long chatId, String command, String username) {
        Command commandObj = commands.getOrDefault(command, commands.get("/register"));
        return commandObj.sendMessage(chatId, username);
    }
}
