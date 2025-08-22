package com.dprol.post_service.kafka.consumer;

import com.dprol.post_service.kafka.event.PostEvent;
import com.dprol.post_service.mapper.PostMapper;
import com.dprol.post_service.redis.service.PostRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PostConsumer implements KafkaConsumer<PostEvent> {

    private final PostMapper postMapper;

    private final PostRedisService postRedisService;

    @Override
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.posts.name}", groupId = "${spring.data.kafka.group-id}")
    public void consume(PostEvent event) {
        switch (event.getStatus()){
            case created, updated -> postRedisService.savePost(postMapper.toPostEvent(event));
            case deleted -> postRedisService.deletePost(event.getPostId());
        }
    }
}
