package com.dprol.post_service.kafka.consumer;

import com.dprol.post_service.kafka.event.LikeEvent;
import com.dprol.post_service.redis.service.PostRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PostLikeConsumer implements KafkaConsumer<LikeEvent> {

    private final PostRedisService postRedisService;

    @Override
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.post-likes.name}", groupId = "${spring.data.kafka.group-id}")
    public void consume(LikeEvent event) {
        switch (event.getStatus()){
            case created -> postRedisService.incrementLikeCount(event.getCommentId());
            case deleted -> postRedisService.decrementLikeCount(event.getCommentId());
        }
    }
}
