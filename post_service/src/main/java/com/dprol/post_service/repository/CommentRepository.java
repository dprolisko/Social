package com.dprol.post_service.repository;

import com.dprol.post_service.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    Optional<Comment> findCommentById(Long commentId);

    Optional<Comment> findByUserId(Long userId);

    List<Comment> getListCommentByUserId(Long userId);

    List<Comment> getListCommentByPostId(Long postId);
}
