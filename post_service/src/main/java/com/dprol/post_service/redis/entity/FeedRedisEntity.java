package com.dprol.post_service.redis.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;

import java.util.NavigableSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("feeds")

public class FeedRedisEntity {

    @Id
    private Long id;

    @Reference
    private NavigableSet<PostRedisEntity> feedPosts;
}
