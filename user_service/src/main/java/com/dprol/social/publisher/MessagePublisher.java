package com.dprol.social.publisher;

public interface MessagePublisher<T>{

    void publisher(T message);
}
