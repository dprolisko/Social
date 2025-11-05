package com.dprol.social.publisher;

import com.dprol.social.event.ProfileViewEvent;
import com.dprol.social.publisher.MessagePublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ProfileViewPublisher implements MessagePublisher<ProfileViewEvent> {

    @Value("${spring.data.channel.profile_view.name}")
    private String channelTopic;

    private final RedisTemplate<String, Object> redisTemplate;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private final NewTopic profileViewTopic;

    @Override
    public void publisher(ProfileViewEvent message) {
        publishToKafka(message);
        publishToRedis(message);
    }

    private void publishToRedis(ProfileViewEvent message) {
        redisTemplate.convertAndSend(channelTopic, message);
    }

    private void publishToKafka(ProfileViewEvent profileViewEvent) {
        try {
            String message = objectMapper.writeValueAsString(profileViewEvent);
            kafkaTemplate.send(profileViewTopic.name(), message);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}