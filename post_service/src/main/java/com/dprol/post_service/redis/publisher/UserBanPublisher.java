package com.dprol.post_service.redis.publisher;

import com.dprol.post_service.redis.entity.UserBanEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserBanPublisher implements RedisPublisher<UserBanEntity> {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.channels.user_ban_channel.name}")
    private String topic;

    @Override
    public void publish(UserBanEntity event) {
        redisTemplate.convertAndSend(topic, event);
    }
}
