package com.dprol.post_service.service;

import com.dprol.post_service.config.ModerationConfig;
import com.dprol.post_service.dto.PostDto;
import com.dprol.post_service.dto.PostHashtagDto;
import com.dprol.post_service.entity.Post;
import com.dprol.post_service.entity.StatusEntity;
import com.dprol.post_service.exception.PostNotFoundException;
import com.dprol.post_service.kafka.event.Status;
import com.dprol.post_service.kafka.producer.PostProducer;
import com.dprol.post_service.mapper.PostMapper;
import com.dprol.post_service.repository.PostRepository;
import com.dprol.post_service.service.hashtag.async.AsyncHashtagService;
import com.dprol.post_service.service.post.PostServiceImpl;
import com.dprol.post_service.service.spelling.SpellingService;
import com.dprol.post_service.validator.post.PostValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

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
    @Mock
    private ModerationConfig moderationConfig;
    @Mock
    private SpellingService spellingService;

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;
    private PostDto postDto;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1L);
        post.setAuthorId(10L);
        post.setContent("Hello world");

        postDto = PostDto.builder().id(1L).authorId(10L).content("Hello world").build();
    }

    @Test
    void createPost_ShouldSaveAndReturnDto() {
        when(postMapper.toEntity(postDto)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(postDto);

        PostDto result = postService.createPost(postDto);

        assertEquals(postDto, result);
        verify(postRepository).save(post);
    }

    @Test
    void deletePost_ShouldDeleteAndSendKafka() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        PostHashtagDto hashtagDto = new PostHashtagDto();
        when(postMapper.toHashtagDto(post)).thenReturn(hashtagDto);

        postService.deletePost(1L);

        verify(postRepository).deleteById(1L);
        verify(asyncHashtagService).addHashtag(hashtagDto);
        verify(postProducer).produce(any());
    }

    @Test
    void findPostById_ShouldReturnPost() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post result = postService.findPostById(1L);

        assertEquals(post, result);
    }

    @Test
    void findPostById_ShouldThrowException_WhenNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> postService.findPostById(1L));
    }

    @Test
    void publishPost_ShouldSetPublishedAndSendKafka() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postMapper.toHashtagDto(post)).thenReturn(new PostHashtagDto());
        when(postMapper.toDto(post)).thenReturn(postDto);

        PostDto result = postService.publishPost(1L);

        assertTrue(post.isPublished());
        assertNotNull(post.getPublishedAt());
        assertEquals(postDto, result);
        verify(postValidator).validatePublicationPost(post);
        verify(postProducer).produce(any());
    }

    @Test
    void updatePost_ShouldValidateAndSendKafka() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(postDto);

        PostDto result = postService.updatePost(1L);

        verify(postValidator).validatePostByVerificationStatus(post);
        verify(postProducer).produce(any());
        assertEquals(postDto, result);
    }

    @Test
    void verifyPost_ShouldSetStatusVerifiedOrNot() {
        Post badPost = new Post();
        badPost.setContent("bad word");

        when(moderationConfig.checkWord("bad word")).thenReturn(true);
        when(moderationConfig.checkWord("Hello world")).thenReturn(false);

        postService.verifyPost(List.of(post, badPost));

        assertEquals(StatusEntity.verified, post.getPostStatus());
        assertEquals(StatusEntity.not_verified, badPost.getPostStatus());
        verify(postRepository, times(2)).save(any(Post.class));
    }

    @Test
    void correctPost_ShouldFixSpelling() throws Exception {
        Post p = new Post();
        p.setContent("helo");
        p.setSpelling(false);

        when(postRepository.getPostsNotVerified()).thenReturn(List.of(p));
        when(spellingService.getSpelling("helo"))
                .thenReturn(CompletableFuture.completedFuture(Optional.of("hello")));

        postService.correctPost();

        assertEquals("hello", p.getContent());
        assertTrue(p.isSpelling());
    }

    @Test
    void findAllAuthorIdsWithNotVerifiedPosts_ShouldReturnIds() {
        Post p1 = new Post(); p1.setAuthorId(1L);
        Post p2 = new Post(); p2.setAuthorId(2L);

        when(postRepository.getPostsNotVerified()).thenReturn(List.of(p1, p2));

        List<Long> result = postService.findAllAuthorIdsWithNotVerifiedPosts();

        assertEquals(List.of(1L, 2L), result);
    }
}
