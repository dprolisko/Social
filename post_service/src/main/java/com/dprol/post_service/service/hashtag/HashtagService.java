package com.dprol.post_service.service.hashtag;

import com.dprol.post_service.dto.HashtagDto;
import com.dprol.post_service.dto.PostHashtagDto;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface HashtagService {

    void addHashtag(String hashtag, PostHashtagDto postHashtagDto);

    void removeHashtag(String hashtag, PostHashtagDto PosthashtagDto);

    void findHashtagByName(String hashtag, PostHashtagDto postHashtagDto);

    Set<PostHashtagDto> getHashtags(String hashtag, Pageable pageable);
}
