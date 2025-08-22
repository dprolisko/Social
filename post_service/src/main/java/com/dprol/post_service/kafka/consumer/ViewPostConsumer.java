package com.dprol.post_service.kafka.consumer;

import com.dprol.post_service.kafka.event.PostEvent;
import com.dprol.post_service.redis.service.PostRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ViewPostConsumer implements KafkaConsumer<PostEvent> {

    private final PostRedisService postRedisService;

    @Override
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.view-count.name}", groupId = "${spring.data.kafka.group-id}")
    public void consume(PostEvent event) {
        postRedisService.incrementViewCount(event.getPostId());
    }
}
