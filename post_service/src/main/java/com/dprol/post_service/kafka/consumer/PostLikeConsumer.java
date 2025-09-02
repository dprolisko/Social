package com.dprol.post_service.kafka.consumer;

import com.dprol.post_service.kafka.event.like.PostLikeEvent;
import com.dprol.post_service.redis.service.PostRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PostLikeConsumer implements KafkaConsumer<PostLikeEvent> {

    private final PostRedisService postRedisService;

    @Override
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.post-likes.name}", groupId = "${spring.data.kafka.group-id}")
    public void consume(PostLikeEvent event) {
        switch (event.getStatus()){
            case created -> postRedisService.incrementLikeCount(event.getPostId());
            case deleted -> postRedisService.decrementLikeCount(event.getPostId());
        }
    }
}
