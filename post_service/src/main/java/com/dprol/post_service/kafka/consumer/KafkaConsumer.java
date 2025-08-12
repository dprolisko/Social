package com.dprol.post_service.kafka.consumer;

import com.dprol.post_service.kafka.event.KafkaEvent;

public interface KafkaConsumer<T extends KafkaEvent> {

    void consume(T event);
}
