package com.dprol.post_service.service.post;

import com.dprol.post_service.dto.PostDto;
import com.dprol.post_service.entity.Post;
import com.dprol.post_service.entity.StatusEntity;
import com.dprol.post_service.kafka.event.Status;
import com.dprol.post_service.kafka.producer.PostProducer;
import com.dprol.post_service.mapper.PostMapper;
import com.dprol.post_service.repository.PostRepository;
import com.dprol.post_service.validator.PostValidator;
import exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    private final PostValidator postValidator;

    private final PostProducer postProducer;

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public void deletePost(Long postId) {
        Post post = findPostById(postId);
        postRepository.deleteById(postId);
        sendToKafka(post, Status.deleted);
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
        Post post = findPostById(postId);
        postValidator.validatePublicationPost(post);
        post.setPublished(true);
        post.setPublishedAt(LocalDateTime.now());
        postRepository.save(post);
        sendToKafka(post, Status.created);
        return postMapper.toDto(post);
    }

    @Override
    public PostDto updatePost(Long postId) {
        Post postValid = findPostById(postId);
        postValidator.validatePostByVereficationStatus(postValid);
        postRepository.save(postValid);
        sendToKafka(postValid, Status.updated);
        return postMapper.toDto(postValid);
    }

    @Override
    public void verifyPost(List<Post> posts) {

    }

    @Override
    public void correctPost(List<Post> posts) {

    }

    private void sendToKafka(Post post, Status status){
        if (post.getAuthorId() != null){
            postProducer.produce(postMapper.toKafkaEvent(post, status));
        }
    }
}
