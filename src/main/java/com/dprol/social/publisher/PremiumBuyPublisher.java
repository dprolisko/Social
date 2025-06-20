package com.dprol.social.publisher;

import com.dprol.social.event.PremiumBuyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PremiumBuyPublisher implements MessagePublisher<PremiumBuyEvent> {

    @Value("${spring.data.channel.premium_bought.name}")
    private String channelTopic;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publisher(PremiumBuyEvent message) {
        redisTemplate.convertAndSend(channelTopic, message);
    }
}
