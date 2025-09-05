package com.dprol.post_service.service.resources;

import com.dprol.post_service.dto.ResourceDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface ResourceService {

    ResourceDto addResource(Long postId, Long userId, String key, List<MultipartFile> files);

    void deleteResource(String key, Long userId);

    InputStream downloadResource(String key);
}
