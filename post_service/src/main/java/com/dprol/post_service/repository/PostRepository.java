package com.dprol.post_service.repository;

import com.dprol.post_service.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    Optional<Post> findPostById(Long postId);

    Optional<Post> findPostByUserId(Long userId);

    List<Post> getPostsNotVerified();

    List<Post> getAllPostsByUserId(Long userId);

    List<Long> findFeedPost(List<Long> userIds, Pageable pageable);
}
