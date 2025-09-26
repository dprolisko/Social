package com.dprol.post_service.controller;

import com.dprol.post_service.dto.PostDto;
import com.dprol.post_service.entity.Post;
import com.dprol.post_service.service.post.PostService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor

public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public PostDto createPost(@RequestBody PostDto postDto) {
        return postService.createPost(postDto);
    }

    @PutMapping("/update")
    public PostDto updatePost(@RequestBody PostDto postDto) {
        return postService.updatePost(postDto.getId());
    }

    @DeleteMapping("{postId}")
    public void deletePost(@PathVariable @Positive Long postId) {
        postService.deletePost(postId);
    }

    @GetMapping("/find/{postId}")
    public Post findPostById(@PathVariable @Positive Long postId) {
        return postService.findPostById(postId);
    }

    @GetMapping("/findAll/{userId}")
    public List<PostDto> findAllPostsByUserId(@PathVariable @Positive Long userId) {
        return postService.getListPostsByUserId(userId);
    }
}
