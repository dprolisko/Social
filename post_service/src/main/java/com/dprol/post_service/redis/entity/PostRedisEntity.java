package com.dprol.post_service.redis.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.NavigableSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("posts")

public class PostRedisEntity {

    @Id
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime publishedAt;

    private LocalDateTime deletedAt;

    private Long viewCount;

    private Long likeCount;

    @Reference
    @ToString.Exclude
    private AuthorRedisEntity authorPost;

    @Reference
    @ToString.Exclude
    private NavigableSet<CommentRedisEntity> comments;
}
