package com.dprol.social.publisher;

import com.dprol.social.event.GoalComplitedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class GoalComplitedPublisher implements MessagePublisher<GoalComplitedEvent> {

    @Value("${spring.data.channel.goal_complete.name}")
    private String channelTopic;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publisher(GoalComplitedEvent message) {
        redisTemplate.convertAndSend(channelTopic, message);
    }
}
