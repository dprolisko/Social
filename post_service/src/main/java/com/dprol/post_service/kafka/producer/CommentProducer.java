package com.dprol.post_service.kafka.producer;

import com.dprol.post_service.kafka.event.CommentEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component

public class CommentProducer extends AbstractKafkaProducer<CommentEvent>{

    @Value("${spring.data.kafka.topics.topic-settings.comments.name}")
    private String topic;

    public CommentProducer(KafkaTemplate<String, Object> kafkaTemplate, Map<String, NewTopic> producerProps) {
        super(kafkaTemplate, producerProps);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
