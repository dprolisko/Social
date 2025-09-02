package com.dprol.post_service.kafka.producer.like;

import com.dprol.post_service.kafka.event.like.PostLikeEvent;
import com.dprol.post_service.kafka.producer.AbstractKafkaProducer;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component

public class PostLikeProducer extends AbstractKafkaProducer<PostLikeEvent> {

    @Value("${spring.data.kafka.topics.topic-settings.post-likes.name}")
    private String topic;

    public PostLikeProducer(KafkaTemplate<String, PostLikeEvent> kafkaTemplate, Map<String, NewTopic> producerProps) {
        super(kafkaTemplate, producerProps);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
