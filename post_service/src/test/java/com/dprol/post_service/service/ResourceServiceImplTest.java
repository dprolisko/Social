package com.dprol.post_service.service;

import com.dprol.post_service.config.AmazonS3Config;
import com.dprol.post_service.dto.ResourceDto;
import com.dprol.post_service.entity.Post;
import com.dprol.post_service.entity.Resources;
import com.dprol.post_service.exception.PostNotFoundException;
import com.dprol.post_service.exception.ResourceNotFoundException;
import com.dprol.post_service.mapper.ResourcesMapper;
import com.dprol.post_service.repository.PostRepository;
import com.dprol.post_service.repository.ResourcesRepository;
import com.dprol.post_service.service.resources.ResourceServiceImpl;
import com.dprol.post_service.service.s3.S3Service;
import com.dprol.post_service.validator.resources.ResourcesValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResourceServiceImplTest {

    @Mock
    private ResourcesRepository resourcesRepository;
    @Mock
    private ResourcesMapper resourcesMapper;
    @Mock
    private ResourcesValidator resourcesValidator;
    @Mock
    private PostRepository postRepository;
    @Mock
    private S3Service s3Service;
    @Mock
    private AmazonS3Config s3Client;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    private Post post;
    private Resources resources;
    private ResourceDto resourceDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        post = new Post();
        post.setId(1L);

        resources = new Resources();
        resources.setId(2L);

        resourceDto = new ResourceDto();
        resourceDto.setId(2L);

        when(s3Client.getBucketName()).thenReturn("bucket-test");
    }

    @Test
    void addResource_ShouldThrow_WhenPostNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class,
                () -> resourceService.addResource(1L, 2L, "key1", List.of()));
    }

    @Test
    void addResource_ShouldThrow_WhenResourceNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(resourcesRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> resourceService.addResource(1L, 2L, "key1", List.of()));
    }

    @Test
    void deleteResource_ShouldDeleteAndRemoveFromS3() {
        Resources res = new Resources();
        res.setId(2L);
        when(resourcesRepository.findByKey("key1")).thenReturn(res);

        resourceService.deleteResource("key1", 2L);

        verify(resourcesValidator).validateResourcesByKey("key1");
        verify(resourcesRepository).deleteByKey("key1");
        verify(s3Service).deleteFile("bucket-test", "key1");
    }

    @Test
    void downloadResource_ShouldReturnInputStream() {
        InputStream expected = new ByteArrayInputStream("data".getBytes());
        when(s3Service.downloadFile("bucket-test", "key1")).thenReturn(expected);

        InputStream result = resourceService.downloadResource("key1");

        verify(resourcesValidator).validateResourcesByKey("key1");
        assertSame(expected, result);
    }
}
