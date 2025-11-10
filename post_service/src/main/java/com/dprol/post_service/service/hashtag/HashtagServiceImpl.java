package com.dprol.post_service.service.hashtag;

import com.dprol.post_service.dto.PostHashtagDto;
import com.dprol.post_service.entity.Hashtag;
import com.dprol.post_service.entity.Post;
import com.dprol.post_service.mapper.PostMapper;
import com.dprol.post_service.redis.service.HashtagRedisService;
import com.dprol.post_service.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    private final PostMapper postMapper;

    private final HashtagRedisService hashtagRedisService;

    private Hashtag buildHashtag(String hashtag, Post post) {
        return Hashtag.builder().hashtag(hashtag).createdAt(LocalDateTime.now()).post(post).build();
    }


    @Override
    public void addHashtag(String hashtag, PostHashtagDto postHashtagDto) {
        Post post = postMapper.toEntity(postHashtagDto);
        Hashtag hashtagEntity = buildHashtag(hashtag, post);
        hashtagRepository.save(hashtagEntity);
    }

    @Override
    public void removeHashtag(String hashtag, PostHashtagDto PosthashtagDto) {
        hashtagRepository.deleteByHashtagAndPostId(hashtag, PosthashtagDto.getId());
    }


    @Override
    public void findHashtagByName(String hashtag, PostHashtagDto postHashtagDto) {
        hashtagRepository.findHashtagByName(hashtag);
    }

    @Override
    public Set<PostHashtagDto> getHashtags(String hashtag, Pageable pageable) {
        Set<PostHashtagDto> hashtags = hashtagRedisService.getHashtags(hashtag, pageable);
        if (hashtags == null) {
            Page<Hashtag> pageHashtag = hashtagRepository.findHashtag(hashtag, pageable);
            return pageHashtag.stream().map(Hashtag::getPost).map(postMapper::toHashtagDto).collect(Collectors.toSet());
        }
        return hashtags.stream().collect(Collectors.toSet());
    }
}
