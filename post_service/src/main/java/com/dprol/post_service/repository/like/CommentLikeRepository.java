package com.dprol.post_service.repository.like;

import com.dprol.post_service.entity.like.CommentLike;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends CrudRepository<CommentLike, Long> {
}
