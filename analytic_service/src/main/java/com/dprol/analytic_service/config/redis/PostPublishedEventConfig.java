package com.dprol.analytic_service.config.redis;

import com.dprol.analytic_service.listener.PostPublishedListener;
import com.dprol.analytic_service.listener.PostViewListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration

public class PostPublishedEventConfig {

    @Value("${spring.data.redis.channel.post-published.name}")
    private String postPublishedEventTopic;

    @Bean
    public MessageListenerAdapter postPublishedEventMessageListenerAdapter(PostPublishedListener postPublishedListener) {
        return new MessageListenerAdapter(postPublishedListener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> postPublishedEventTopicMessageListenerAdapter(@Qualifier("postPublishedEventMessageListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        return Pair.of(new ChannelTopic(postPublishedEventTopic), messageListenerAdapter);
    }
}
