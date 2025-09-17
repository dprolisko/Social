package com.dprol.post_service.redis.publisher;

import com.dprol.post_service.redis.entity.RedisEvent;

public interface RedisPublisher <T extends RedisEvent> {

    void publish(T event);
}
