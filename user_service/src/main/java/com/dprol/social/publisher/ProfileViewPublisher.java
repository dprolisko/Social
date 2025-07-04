package com.dprol.social.publisher;

import com.dprol.social.event.ProfileViewEvent;
import com.dprol.social.publisher.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ProfileViewPublisher implements MessagePublisher<ProfileViewEvent> {

    @Value("${spring.data.channel.goal_complete.name}")
    private String channelTopic;

    private final RedisTemplate<String, Object> redisTemplate;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publisher(ProfileViewEvent message) {
        publishToKafka(message);
        publishToRedis(message);
    }

    private void publishToRedis(ProfileViewEvent message) {
        redisTemplate.convertAndSend(channelTopic, message);
    }

    private void publishToKafka(ProfileViewEvent message) {
        kafkaTemplate.send(channelTopic, message);
    }
}