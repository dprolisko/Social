package com.dprol.analytic_service.config.redis;

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

public class PostViewEventConfig {

    @Value("${spring.data.redis.channel.post-view.name}")
    private String postViewEventTopic;

    @Bean
    public MessageListenerAdapter postViewEventMessageListenerAdapter(PostViewListener postViewListener) {
        return new MessageListenerAdapter(postViewListener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> postViewEventTopicMessageListenerAdapter(@Qualifier("postViewEventMessageListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        return Pair.of(new ChannelTopic(postViewEventTopic), messageListenerAdapter);
    }
}
