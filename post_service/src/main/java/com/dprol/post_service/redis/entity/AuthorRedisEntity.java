package com.dprol.post_service.redis.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("authors")

public class AuthorRedisEntity {

    private Long id;

    private String name;

    private String smallFileId;
}
