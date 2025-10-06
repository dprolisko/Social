package com.dprol.notification_service.service;

import com.dprol.notification_service.entity.TelegramProfile;

import java.util.Optional;

public interface TelegramProfileService {

    void saveTelegramProfile(TelegramProfile profile);

    TelegramProfile findByUserId(Long userId);

    Optional<TelegramProfile> findByChatId(Long chatId);

    boolean existsByChatId(Long chatId);
}
