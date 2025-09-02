package com.dprol.post_service.repository.like;

import com.dprol.post_service.entity.like.PostLike;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends CrudRepository<PostLike, Long> {

    void deletePostIdAndUserId(Long postId, Long userId);
}
