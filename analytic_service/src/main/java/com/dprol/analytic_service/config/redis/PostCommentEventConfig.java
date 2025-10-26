package com.dprol.analytic_service.config.redis;

import com.dprol.analytic_service.listener.PostCommentListener;
import com.dprol.analytic_service.listener.PostLikeListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration

public class PostCommentEventConfig {

    @Value("${spring.data.redis.channel.post-comment.name}")
    private String postCommentEventTopic;

    @Bean
    public MessageListenerAdapter postCommentEventMessageListenerAdapter(PostCommentListener postCommentListener) {
        return new MessageListenerAdapter(postCommentListener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> postCommentEventTopicMessageListenerAdapter(@Qualifier("postCommentEventMessageListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        return Pair.of(new ChannelTopic(postCommentEventTopic), messageListenerAdapter);
    }
}
