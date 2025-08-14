package com.dprol.post_service.kafka.producer;

import com.dprol.post_service.kafka.event.LikeEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component

public class LikeProducer extends AbstractKafkaProducer<LikeEvent>{

    @Value("${spring.data.kafka.topics.topic-settings.likes.name}")
    private String topic;

    public LikeProducer(KafkaTemplate<String, LikeEvent> kafkaTemplate, Map<String, NewTopic> producerProps) {
        super(kafkaTemplate, producerProps);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
