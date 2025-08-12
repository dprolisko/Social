package com.dprol.post_service.kafka.producer;

import com.dprol.post_service.kafka.event.KafkaEvent;

public interface KafkaProducer<T extends KafkaEvent> {

    void produce(T event);
}
