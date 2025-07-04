package com.dprol.social.publisher;

import com.dprol.social.event.SubscriptionEvent;
import com.dprol.social.publisher.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class SubscriptionPublisher implements MessagePublisher<SubscriptionEvent> {

    @Value("${spring.data.channel.follower.name}")
    private String channelTopic;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publisher(SubscriptionEvent message) {
        redisTemplate.convertAndSend(channelTopic, message);
    }
}
