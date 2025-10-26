package com.dprol.analytic_service.config.redis;


import com.dprol.analytic_service.listener.AchievementReceivedListener;
import com.dprol.analytic_service.listener.FollowerListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration

public class FollowerEventConfig {

    @Value("${spring.data.redis.channel.follower.name}")
    private String followerEventTopic;

    @Bean
    public MessageListenerAdapter followerEventMessageListenerAdapter(FollowerListener followerListener) {
        return new MessageListenerAdapter(followerListener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> followerEventTopicMessageListenerAdapter(@Qualifier("followerEventMessageListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        return Pair.of(new ChannelTopic(followerEventTopic), messageListenerAdapter);
    }
}
