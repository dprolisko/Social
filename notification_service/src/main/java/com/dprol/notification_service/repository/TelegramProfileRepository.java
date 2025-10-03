package com.dprol.notification_service.repository;

import com.dprol.notification_service.entity.TelegramProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface TelegramProfileRepository extends JpaRepository<TelegramProfile, Long> {

    Optional<TelegramProfile> findByUserId(Long userId);

    Optional<TelegramProfile> findByTelegramId(Long telegramId);

    boolean existsByTelegramId(Long telegramId);
}
