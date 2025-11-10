package com.dprol.post_service.redis.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@AllArgsConstructor

public class UserBanEntity implements RedisEvent {

    private List<Long> userIds;
}
