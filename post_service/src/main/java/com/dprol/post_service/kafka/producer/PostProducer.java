package com.dprol.post_service.kafka.producer;

import com.dprol.post_service.kafka.event.PostEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component

public class PostProducer extends AbstractKafkaProducer<PostEvent> {

    @Value("${spring.data.kafka.topics.topic-settings.posts.name}")
    private String topic;

    public PostProducer(KafkaTemplate<String, Object> kafkaTemplate, Map<String, NewTopic> producerProps) {
        super(kafkaTemplate, producerProps);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
