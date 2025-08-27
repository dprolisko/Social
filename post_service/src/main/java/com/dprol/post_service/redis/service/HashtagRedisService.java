package com.dprol.post_service.redis.service;

import com.dprol.post_service.dto.PostHashtagDto;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface HashtagRedisService {

    void addHashtag(String hashtag, PostHashtagDto postHashtagDto);

    void removeHashtag(String hashtag, PostHashtagDto postHashtagDto);

    Set<PostHashtagDto> getHashtags(String hashtag, Pageable pageable);
}
