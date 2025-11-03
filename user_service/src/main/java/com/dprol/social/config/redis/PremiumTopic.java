package com.dprol.social.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration

public class PremiumTopic {

    @Bean
    public ChannelTopic channelPremiumTopic(@Value("${spring.data.channel.premium_bought.name}") String name) {
        return new ChannelTopic(name);
    }
}
