package com.dprol.post_service.service;

import com.dprol.post_service.dto.PostHashtagDto;
import com.dprol.post_service.entity.Hashtag;
import com.dprol.post_service.entity.Post;
import com.dprol.post_service.mapper.PostMapper;
import com.dprol.post_service.redis.service.HashtagRedisService;
import com.dprol.post_service.repository.HashtagRepository;
import com.dprol.post_service.service.hashtag.HashtagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HashtagServiceImplTest {

    @Mock
    private HashtagRepository hashtagRepository;
    @Mock
    private PostMapper postMapper;
    @Mock
    private HashtagRedisService hashtagRedisService;

    @InjectMocks
    private HashtagServiceImpl hashtagService;

    private PostHashtagDto postHashtagDto;
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        postHashtagDto = new PostHashtagDto();
        postHashtagDto.setId(1L);
        postHashtagDto.setContent("Hello #java");

        post = new Post();
        post.setId(1L);
        post.setContent("Hello #java");
    }

    @Test
    void addHashtag_ShouldSaveEntity() {
        when(postMapper.toEntity(postHashtagDto)).thenReturn(post);

        hashtagService.addHashtag("java", postHashtagDto);

        verify(postMapper).toEntity(postHashtagDto);
        verify(hashtagRepository).save(any(Hashtag.class));
    }

    @Test
    void removeHashtag_ShouldCallRepositoryDelete() {
        hashtagService.removeHashtag("java", postHashtagDto);

        verify(hashtagRepository).deleteHashtagAndPostId("java", 1L);
    }

    @Test
    void findHashtagByName_ShouldCallRepository() {
        hashtagService.findHashtagByName("java", postHashtagDto);

        verify(hashtagRepository).findHashtagByName("java");
    }

    @Test
    void getHashtags_ShouldReturnFromRedis_WhenCacheExists() {
        Pageable pageable = PageRequest.of(0, 10);
        Set<PostHashtagDto> cached = Set.of(postHashtagDto);

        when(hashtagRedisService.getHashtags("java", pageable)).thenReturn(cached);

        Set<PostHashtagDto> result = hashtagService.getHashtags("java", pageable);

        assertEquals(cached, result);
        verify(hashtagRedisService).getHashtags("java", pageable);
        verifyNoInteractions(postMapper);
        verify(hashtagRepository, never()).findHashtag(anyString(), any());
    }

    @Test
    void getHashtags_ShouldReturnFromDatabase_WhenCacheIsNull() {
        Pageable pageable = PageRequest.of(0, 10);

        when(hashtagRedisService.getHashtags("java", pageable)).thenReturn(null);

        Hashtag hashtagEntity = Hashtag.builder().id(1L).hashtag("java").post(post).build();
        Page<Hashtag> page = new PageImpl<>(List.of(hashtagEntity));

        when(hashtagRepository.findHashtag("java", pageable)).thenReturn(page);
        when(postMapper.toHashtagDto(post)).thenReturn(postHashtagDto);

        Set<PostHashtagDto> result = hashtagService.getHashtags("java", pageable);

        assertEquals(1, result.size());
        assertTrue(result.contains(postHashtagDto));

        verify(hashtagRepository).findHashtag("java", pageable);
        verify(postMapper).toHashtagDto(post);
    }
}
