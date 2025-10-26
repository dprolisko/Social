package com.dprol.analytic_service.config.redis;

import com.dprol.analytic_service.listener.FollowerListener;
import com.dprol.analytic_service.listener.GoalCompletedListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration

public class GoalCompletedEventConfig {

    @Value("${spring.data.redis.channel.goal-complete.name}")
    private String goalCompletedEventTopic;

    @Bean
    public MessageListenerAdapter goalCompletedEventMessageListenerAdapter(GoalCompletedListener goalCompletedListener) {
        return new MessageListenerAdapter(goalCompletedListener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> goalCompletedEventTopicMessageListenerAdapter(@Qualifier("goalCompletedEventMessageListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        return Pair.of(new ChannelTopic(goalCompletedEventTopic), messageListenerAdapter);
    }
}
