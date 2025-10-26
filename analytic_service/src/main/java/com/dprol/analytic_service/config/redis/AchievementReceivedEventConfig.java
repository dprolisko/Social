package com.dprol.analytic_service.config.redis;

import com.dprol.analytic_service.listener.AchievementReceivedListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration

public class AchievementReceivedEventConfig {

    @Value("${spring.data.redis.channel.achievement-received.name}")
    private String achievementReceivedEventTopic;

    @Bean
    public MessageListenerAdapter achievementReceivedEventMessageListenerAdapter(AchievementReceivedListener achievementReceivedListener) {
        return new MessageListenerAdapter(achievementReceivedListener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> achievementReceivedEventTopicMessageListenerAdapter(@Qualifier("achievementReceivedEventMessageListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        return Pair.of(new ChannelTopic(achievementReceivedEventTopic), messageListenerAdapter);
    }
}
