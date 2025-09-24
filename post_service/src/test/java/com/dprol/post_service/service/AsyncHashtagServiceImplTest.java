package com.dprol.post_service.service;

import com.dprol.post_service.dto.PostHashtagDto;
import com.dprol.post_service.service.hashtag.HashtagService;
import com.dprol.post_service.service.hashtag.async.AsyncHashtagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AsyncHashtagServiceImplTest {

    @Mock
    private HashtagService hashtagService;

    @InjectMocks
    private AsyncHashtagServiceImpl asyncHashtagService;

    private PostHashtagDto postHashtagDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        postHashtagDto = new PostHashtagDto();
        postHashtagDto.setContent("Hello #java #spring");
    }

    @Test
    void addHashtag_ShouldExtractAndCallService() {
        asyncHashtagService.addHashtag(postHashtagDto);

        // Проверяем, что вызвался addHashtag для каждого хэштега
        verify(hashtagService).addHashtag(eq("java"), eq(postHashtagDto));
        verify(hashtagService).addHashtag(eq("spring"), eq(postHashtagDto));
    }

    @Test
    void removeHashtag_ShouldExtractAndCallService() {
        asyncHashtagService.removeHashtag(postHashtagDto);

        verify(hashtagService).removeHashtag(eq("java"), eq(postHashtagDto));
        verify(hashtagService).removeHashtag(eq("spring"), eq(postHashtagDto));
    }

    @Test
    void getHashtag_ShouldReturnFromService() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Set<PostHashtagDto> expected = Set.of(postHashtagDto);

        when(hashtagService.getHashtags("java", pageable)).thenReturn(expected);

        CompletableFuture<Set<PostHashtagDto>> future = asyncHashtagService.getHashtag("java", pageable);

        Set<PostHashtagDto> result = future.get();

        assertEquals(expected, result);
        verify(hashtagService).getHashtags("java", pageable);
    }
}
