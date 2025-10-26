package com.dprol.analytic_service.config.redis;

import com.dprol.analytic_service.listener.PostPublishedListener;
import com.dprol.analytic_service.listener.PremiumBoughtListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration

public class PremiumBoughtEventConfig {

    @Value("${spring.data.redis.channel.premium-bought}")
    private String premiumBoughtEventTopic;

    @Bean
    public MessageListenerAdapter premiumBoughtEventMessageListenerAdapter(PremiumBoughtListener premiumBoughtListener) {
        return new MessageListenerAdapter(premiumBoughtListener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> premiumBoughtEventTopicMessageListenerAdapter(@Qualifier("premiumBoughtEventMessageListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        return Pair.of(new ChannelTopic(premiumBoughtEventTopic), messageListenerAdapter);
    }
}
