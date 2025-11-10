package com.dprol.post_service.kafka.producer;

import com.dprol.post_service.kafka.event.Feed;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

public class FeedProducer extends AbstractKafkaProducer<Feed>{

    @Value("${spring.data.kafka.topics.topic-settings.feed-heater.name}")
    private String topic;

    public FeedProducer(KafkaTemplate<String, Object> kafkaTemplate, Map<String, NewTopic> producerProps) {
        super(kafkaTemplate, producerProps);
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
