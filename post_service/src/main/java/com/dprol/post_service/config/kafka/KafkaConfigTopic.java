package com.dprol.post_service.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class KafkaConfigTopic {

    @Value("${spring.data.kafka.topics.topic-settings.comments.name}")
    private String commentTopicName;

    @Value("${spring.data.kafka.topics.topic-settings.likes.name}")
    private String likeTopicName;

    @Value("${spring.data.kafka.topics.topic-settings.feed-heater.name}")
    private String feedTopicName;

    @Value("${spring.data.kafka.topics.topic-settings.posts.name}")
    private String postTopicName;

    @Bean
    public NewTopic commentTopic() {
        return new NewTopic(commentTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic likeTopic() {
        return new NewTopic(likeTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic feedTopic() {
        return new NewTopic(feedTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic postTopic() {
        return new NewTopic(postTopicName, 1, (short) 1);
    }
}
