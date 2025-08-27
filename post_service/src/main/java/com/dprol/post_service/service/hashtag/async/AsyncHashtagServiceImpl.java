package com.dprol.post_service.service.hashtag.async;

import com.dprol.post_service.dto.PostHashtagDto;
import com.dprol.post_service.service.hashtag.HashtagService;
import com.dprol.post_service.utils.HashtagUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor

public class AsyncHashtagServiceImpl implements AsyncHashtagService {

    private HashtagService hashtagService;

    @Override
    public void addHashtag(PostHashtagDto postHashtagDto) {
        Set<String> hashtags = HashtagUtils.getHashtag(postHashtagDto.getContent());
        hashtags.forEach(hashtag -> hashtagService.addHashtag(hashtag, postHashtagDto));
    }

    @Override
    public void removeHashtag(PostHashtagDto postHashtagDto) {
        Set<String> hashtags = HashtagUtils.getHashtag(postHashtagDto.getContent());
        hashtags.forEach(hashtag -> hashtagService.removeHashtag(hashtag, postHashtagDto));

    }

    @Override
    public CompletableFuture<Set<PostHashtagDto>> getHashtag(String hashtag, Pageable pageable) {
        return CompletableFuture.completedFuture(hashtagService.getHashtags(hashtag, pageable));
    }
}
