package com.dprol.post_service.kafka.consumer;

import com.dprol.post_service.kafka.event.CommentEvent;
import com.dprol.post_service.mapper.CommentMapper;
import com.dprol.post_service.redis.service.CommentRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class CommentConsumer implements KafkaConsumer<CommentEvent> {

    private final CommentRedisService commentRedisService;

    private final CommentMapper commentMapper;

    @Override
    @KafkaListener(topics = "${spring.data.kafka.topics.topic-settings.comments.name}", groupId = "${spring.data.kafka.group-id}")
    public void consume(CommentEvent event) {
        switch (event.getStatus()){
            case created, updated -> commentRedisService.saveComment(commentMapper.toCommentEvent(event));
            case deleted -> commentRedisService.deleteComment(event.getPostId());
        }
    }
}
