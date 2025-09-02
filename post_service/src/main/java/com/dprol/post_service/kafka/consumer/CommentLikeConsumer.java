package com.dprol.post_service.kafka.consumer;

import com.dprol.post_service.kafka.event.like.CommentLikeEvent;
import com.dprol.post_service.redis.service.CommentRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class CommentLikeConsumer implements KafkaConsumer<CommentLikeEvent> {

    private final CommentRedisService commentRedisService;

    @Override
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.comment-likes.name}", groupId = "${spring.data.kafka.group-id}")
    public void consume(CommentLikeEvent event) {
        switch (event.getStatus()){
            case created -> commentRedisService.incrementComment(event.getCommentId());
            case deleted -> commentRedisService.decrementComment(event.getCommentId());
        }
    }
}
