package com.dprol.post_service.redis.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("comments")

public class CommentRedisEntity {

    @Id
    private Long id;

    private String content;

    private Long likeCount;

    private LocalDateTime createdAt;

    private LocalDateTime publishedAt;

    private LocalDateTime deletedAt;

    @Reference
    @ToString.Exclude
    private AuthorRedisEntity authorComment;

    @Reference
    private Long postId;
}
