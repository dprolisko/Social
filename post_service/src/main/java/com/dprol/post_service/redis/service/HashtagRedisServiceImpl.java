package com.dprol.post_service.redis.service;

import com.dprol.post_service.dto.PostHashtagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service

public class HashtagRedisServiceImpl implements HashtagRedisService {

    private final RedisTemplate<String, PostHashtagDto> redisTemplate;

    private final ZSetOperations<String, PostHashtagDto> zSetOperations;

    @Value("${spring.data.redis.cache.settings.max-hashtags-size}")
    private Long maxSize;

    public HashtagRedisServiceImpl(RedisTemplate<String, PostHashtagDto> redisTemplate) {
        this.redisTemplate = redisTemplate;
        zSetOperations = redisTemplate.opsForZSet();
    }

    @Override
    public void addHashtag(String hashtag, PostHashtagDto postHashtagDto) {
        int countSize = postHashtagDto.getLikeCount().size();
        zSetOperations.add(hashtag, postHashtagDto, countSize);
        Long size = zSetOperations.size(hashtag);
        if (size != null && size > maxSize) {
            zSetOperations.popMin(hashtag);
        }
    }

    @Override
    public void removeHashtag(String hashtag, PostHashtagDto postHashtagDto) {
        zSetOperations.remove(hashtag, postHashtagDto);
    }

    @Override
    public Set<PostHashtagDto> getHashtags(String hashtag, Pageable pageable) {
        int count = pageable.getPageNumber() == 0?pageable.getPageSize():pageable.getPageNumber()*pageable.getPageSize();
        if (count <= maxSize && redisTemplate.hasKey(hashtag)) {
            return zSetOperations.reverseRange(hashtag, pageable.getOffset(), pageable.getPageSize());
        }
        return null;
    }
}
