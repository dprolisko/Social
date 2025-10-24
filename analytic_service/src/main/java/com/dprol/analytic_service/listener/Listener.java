package com.dprol.analytic_service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.function.Consumer;

public abstract class Listener<T> implements MessageListener {

    private ObjectMapper objectMapper;

    protected void handleEvent(Message message, Class<T> clazz, Consumer<T> consumer) {
        try{
            T t = objectMapper.readValue(message.getBody(), clazz);
            consumer.accept(t);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
