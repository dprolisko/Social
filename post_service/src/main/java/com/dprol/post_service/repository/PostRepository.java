package com.dprol.post_service.repository;

import com.dprol.post_service.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.postStatus = 'not_verified'")
    List<Post> getPostsNotVerified();

    @Query("SELECT p FROM Post p WHERE p.authorId = : userId")
    List<Post> getAllPostsByUserId(Long userId);

    @Query(nativeQuery = true, value = """
            SELECT p.id FROM post p
            WHERE p.authorId IN :userIds AND p.published = true
            ORDER BY p.publishedAt DESC
            """)
    List<Long> findFeedPost(List<Long> userIds, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postStatus = 'unchecked'")
    List<Post> findAllUncheckedPosts();
}
