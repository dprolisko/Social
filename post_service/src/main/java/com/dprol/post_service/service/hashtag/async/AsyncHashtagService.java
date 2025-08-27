package com.dprol.post_service.service.hashtag.async;

import com.dprol.post_service.dto.PostHashtagDto;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface AsyncHashtagService {

    void addHashtag(PostHashtagDto postHashtagDto);

    void removeHashtag(PostHashtagDto postHashtagDto);

    CompletableFuture<Set<PostHashtagDto>> getHashtag(String hashtag, Pageable pageable);
}
