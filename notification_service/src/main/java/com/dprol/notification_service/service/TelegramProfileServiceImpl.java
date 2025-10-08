package com.dprol.notification_service.service;

import com.dprol.notification_service.entity.TelegramProfile;
import com.dprol.notification_service.exception.NotFoundException;
import com.dprol.notification_service.repository.TelegramProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class TelegramProfileServiceImpl implements TelegramProfileService {

    private final TelegramProfileRepository telegramProfileRepository;


    @Override
    public void saveTelegramProfile(TelegramProfile profile) {
        telegramProfileRepository.save(profile);
    }

    @Override
    public TelegramProfile findByUserId(Long userId) {
        return telegramProfileRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    public Optional<TelegramProfile> findByChatId(Long chatId) {
        return telegramProfileRepository.findByTelegramId(chatId);
    }

    @Override
    public boolean existsByChatId(Long chatId) {
        return telegramProfileRepository.existsByTelegramId(chatId);
    }
}
