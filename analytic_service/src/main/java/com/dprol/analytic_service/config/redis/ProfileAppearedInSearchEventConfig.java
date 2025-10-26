package com.dprol.analytic_service.config.redis;

import com.dprol.analytic_service.listener.PremiumBoughtListener;
import com.dprol.analytic_service.listener.ProfileAppearedInSearchListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration

public class ProfileAppearedInSearchEventConfig {

    @Value("${spring.data.redis.channel.profile-appeared-in-search.name}")
    private String profileAppearedInSearchEventTopic;

    @Bean
    public MessageListenerAdapter profileAppearedInSearchEventMessageListenerAdapter(ProfileAppearedInSearchListener profileAppearedInSearchListener) {
        return new MessageListenerAdapter(profileAppearedInSearchListener);
    }

    @Bean
    public Pair<Topic, MessageListenerAdapter> profileAppearedInSearchEventTopicMessageListenerAdapter(@Qualifier("profileAppearedInSearchEventMessageListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        return Pair.of(new ChannelTopic(profileAppearedInSearchEventTopic), messageListenerAdapter);
    }
}
