package com.dprol.post_service.service.resources;

import com.amazonaws.auth.policy.Resource;
import com.amazonaws.services.s3.AmazonS3;
import com.dprol.post_service.config.AmazonS3Config;
import com.dprol.post_service.dto.ResourceDto;
import com.dprol.post_service.entity.Post;
import com.dprol.post_service.entity.Resources;
import com.dprol.post_service.exception.PostNotFoundException;
import com.dprol.post_service.exception.ResourceNotFoundException;
import com.dprol.post_service.mapper.ResourcesMapper;
import com.dprol.post_service.repository.PostRepository;
import com.dprol.post_service.repository.ResourcesRepository;
import com.dprol.post_service.service.s3.S3Service;
import com.dprol.post_service.validator.resources.ResourcesValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ResourceServiceImpl implements ResourceService {

    private final ResourcesRepository resourcesRepository;

    private final ResourcesMapper resourcesMapper;

    private final ResourcesValidator resourcesValidator;

    private final PostRepository postRepository;

    private final S3Service s3Service;

    private final AmazonS3Config s3Client;


    @Override
    public ResourceDto addResource(Long postId, Long userId, String key, List<MultipartFile> files) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));
        resourcesValidator.validateResourcesByKey(key);
        Resources resources = resourcesRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        resources.setPost(post);
        resourcesRepository.save(resources);
        s3Service.uploadFile((MultipartFile) files);
        return resourcesMapper.toDto(resources);
    }

    @Override
    public void deleteResource(String key, Long userId) {
        Resources resource = resourcesRepository.findByKey(key);
        resourcesValidator.validateResourcesByKey(key);
        resourcesRepository.deleteByKey(key);
        s3Service.deleteFile(s3Client.getBucketName(),key);
    }

    @Override
    public InputStream downloadResource(String key) {
        resourcesValidator.validateResourcesByKey(key);
        return s3Service.downloadFile(s3Client.getBucketName(),key);
    }
}
