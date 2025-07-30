package com.dprol.post_service.repository;

import com.dprol.post_service.entity.Like;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends CrudRepository<Like, Long> {

    void deleteCommentIdAndUserId(Long commentId, Long userId);

    void deletePostIdAndUserId(Long postId, Long userId);
}
