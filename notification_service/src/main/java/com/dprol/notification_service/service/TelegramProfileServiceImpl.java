package com.dprol.notification_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Service
@RequiredArgsConstructor

public class TelegramProfileServiceImpl extends TelegramBotsLongPollingApplication {

    private final TelegramProfileService telegramProfileService;


}
