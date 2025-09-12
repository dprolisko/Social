package com.dprol.post_service.controller;

import com.dprol.post_service.dto.PostDto;
import com.dprol.post_service.entity.Post;
import com.dprol.post_service.service.post.PostService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor

public class PostController {

    private final PostService postService;

    @PostMapping
    public PostDto createPost(@PathVariable PostDto postDto) {
        return postService.createPost(postDto);
    }

    @PutMapping
    public PostDto updatePost(@PathVariable PostDto postDto) {
        return postService.updatePost(postDto.getId());
    }

    @DeleteMapping("{postId}")
    public void deletePost(@PathVariable @Positive Long postId) {
        postService.deletePost(postId);
    }

    @GetMapping("{postId}")
    public Post findPostById(@PathVariable @Positive Long postId) {
        return postService.findPostById(postId);
    }

    @GetMapping("{userId}")
    public List<PostDto> findAllPostsByUserId(@PathVariable @Positive Long userId) {
        return postService.getListPostsByUserId(userId);
    }
}
