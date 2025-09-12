package com.dprol.post_service.controller;

import com.dprol.post_service.dto.ResourceDto;
import com.dprol.post_service.service.resources.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("resources")
@RequiredArgsConstructor

public class ResourcesController {

    private final ResourceService resourceService;

    @PostMapping("{postId}")
    public ResourceDto createResource(Long postId, Long userId, String key, List<MultipartFile> files) {
        return resourceService.addResource(postId, userId, key, files);
    }

    @DeleteMapping("{key}")
    public void deleteResource(Long userId, String key) {
        resourceService.deleteResource(key, userId);
    }

    @GetMapping("download/{key}")
    public InputStream downloadResource(String key) {
        return resourceService.downloadResource(key);
    }
}
