package com.dprol.post_service.kafka.producer;

import com.dprol.post_service.kafka.event.KafkaEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

@RequiredArgsConstructor

public abstract class AbstractKafkaProducer<T extends KafkaEvent> implements KafkaProducer<T> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final Map<String, NewTopic> producerProps;

    @Override
    public void produce(T event) {
        NewTopic topicProduce = producerProps.get(getTopic());
        kafkaTemplate.send(topicProduce.name(), event);
    }

    public abstract String getTopic();
}
