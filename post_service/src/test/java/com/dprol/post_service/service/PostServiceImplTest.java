package com.dprol.post_service.service;

import com.dprol.post_service.dto.PostDto;
import com.dprol.post_service.dto.PostHashtagDto;
import com.dprol.post_service.entity.Post;
import com.dprol.post_service.exception.PostNotFoundException;
import com.dprol.post_service.kafka.producer.PostProducer;
import com.dprol.post_service.mapper.PostMapper;
import com.dprol.post_service.repository.PostRepository;
import com.dprol.post_service.service.hashtag.async.AsyncHashtagService;
import com.dprol.post_service.service.post.PostServiceImpl;
import com.dprol.post_service.validator.post.PostValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private PostValidator postValidator;

    @Mock
    private PostProducer postProducer;

    @Mock
    private AsyncHashtagService asyncHashtagService;

    // Примерные объекты
    private PostDto postDto;
    private Post post;

    @BeforeEach
    void setUp() {
        postDto = new PostDto(); // заполни нужные поля
        post = new Post();       // заполни нужные поля
        post.setAuthorId(1L);
    }

    @Test
    void createPost_shouldSaveAndReturnDto() {
        when(postMapper.toEntity(postDto)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(postDto);

        PostDto result = postService.createPost(postDto);

        assertEquals(postDto, result);
        verify(postRepository).save(post);
    }

    @Test
    void deletePost_shouldDeleteAndTriggerAsyncAndKafka() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        PostHashtagDto hashtagDto = new PostHashtagDto();
        when(postMapper.toHashtagDto(post)).thenReturn(hashtagDto);

        postService.deletePost(postId);

        verify(postRepository).deleteById(postId);
        verify(asyncHashtagService).addHashtag(hashtagDto);
        verify(postProducer).produce(any());
    }

    @Test
    void getListPostsByUserId_shouldReturnMappedList() {
        Long userId = 1L;
        List<Post> posts = List.of(post);
        when(postRepository.getAllPostsByUserId(userId)).thenReturn(posts);
        when(postMapper.toDto(post)).thenReturn(postDto);

        List<PostDto> result = postService.getListPostsByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(postDto, result.get(0));
    }

    @Test
    void findPostById_shouldReturnPost() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Post result = postService.findPostById(postId);

        assertEquals(post, result);
    }

    @Test
    void findPostById_shouldThrowExceptionIfNotFound() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.findPostById(postId));
    }

    @Test
    void publishPost_shouldValidateSaveAndSendKafka() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        PostHashtagDto hashtagDto = new PostHashtagDto();
        when(postMapper.toHashtagDto(post)).thenReturn(hashtagDto);
        when(postMapper.toDto(post)).thenReturn(postDto);

        PostDto result = postService.publishPost(postId);

        assertTrue(post.isPublished());
        assertNotNull(post.getPublishedAt());
        verify(postValidator).validatePublicationPost(post);
        verify(postRepository).save(post);
        verify(asyncHashtagService).addHashtag(hashtagDto);
        verify(postProducer).produce(any());
        assertEquals(postDto, result);
    }

    @Test
    void updatePost_shouldValidateAndSendKafka() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(postDto);

        PostDto result = postService.updatePost(postId);

        verify(postValidator).validatePostByVerificationStatus(post);
        verify(postRepository).save(post);
        verify(postProducer).produce(any());
        assertEquals(postDto, result);
    }
}

