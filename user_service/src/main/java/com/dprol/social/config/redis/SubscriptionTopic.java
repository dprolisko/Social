package com.dprol.social.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration

public class SubscriptionTopic {

    @Bean
    public ChannelTopic channelSubscriptionTopic(@Value("${spring.data.channel.follower.name}") String name) {
        return new ChannelTopic(name);
    }
}
