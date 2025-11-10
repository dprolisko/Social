package com.dprol.post_service.kafka.producer.like;

import com.dprol.post_service.kafka.event.like.CommentLikeEvent;
import com.dprol.post_service.kafka.producer.AbstractKafkaProducer;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component

public class CommentLikeProducer extends AbstractKafkaProducer<CommentLikeEvent> {

    @Value("${spring.data.kafka.topics.topic-settings.comment-likes.name}")
    private String topic;

    public CommentLikeProducer(KafkaTemplate<String, Object> kafkaTemplate, Map<String, NewTopic> producerProps) {
        super(kafkaTemplate, producerProps);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}