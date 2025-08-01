package com.dprol.social.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class ProfileViewKafkaTopic {

    @Bean
    public NewTopic newTopic(@Value("${spring.data.channel.profile_view.name}") String name) {
        return new NewTopic(name, 1, (short) 1);
    }
}
