package com.dprol.post_service.service;

import com.dprol.post_service.dto.PostDto;
import com.dprol.post_service.entity.Post;
import com.dprol.post_service.mapper.PostMapper;
import com.dprol.post_service.repository.PostRepository;
import exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public List<PostDto> getListPostsByUserId(Long userId) {
        List<Post> posts =  postRepository.getAllPostsByUserId(userId);
        return posts.stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(String.format("Post with id %s not found",postId)));
    }

    @Override
    public PostDto publishPost(Long postId) {
        return null;
    }

    @Override
    public PostDto updatePost(Long postId) {
        return null;
    }

    @Override
    public List<Long> getListPostIdsNotVerified() {
        return List.of();
    }
}
