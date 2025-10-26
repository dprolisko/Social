package com.dprol.analytic_service.config.redis;

import com.dprol.analytic_service.listener.PremiumBoughtListener;
import com.dprol.analytic_service.listener.ProfileViewListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration

public class ProfileViewEventConfig {

    @Value("${spring.data.redis.channel.profile-view.name}")
    private String profileViewEventTopic;

    @Bean
    public MessageListenerAdapter profileViewEventMessageListenerAdapter(ProfileViewListener profileViewListener) {
        return new MessageListenerAdapter(profileViewListener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> profileViewEventTopicMessageListenerAdapter(@Qualifier("profileViewEventMessageListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        return Pair.of(new ChannelTopic(profileViewEventTopic), messageListenerAdapter);
    }
}
