package com.dprol.analytic_service.config.redis;

import com.dprol.analytic_service.listener.PostLikeListener;
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

public class PostLikeEventConfig {

    @Value("${spring.data.redis.channel.like_post.name}")
    private String postLikeEventTopic;

    @Bean
    public MessageListenerAdapter postLikeEventMessageListenerAdapter(PostLikeListener postLikeListener) {
        return new MessageListenerAdapter(postLikeListener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> postLikeEventTopicMessageListenerAdapter(@Qualifier("postLikeEventMessageListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        return Pair.of(new ChannelTopic(postLikeEventTopic), messageListenerAdapter);
    }
}
