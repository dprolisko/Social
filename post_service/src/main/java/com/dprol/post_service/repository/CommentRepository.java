package com.dprol.post_service.repository;

import com.dprol.post_service.entity.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.authorId = : userId")
    List<Comment> getListCommentByUserId(Long userId);

    @Query("SELECT c FROM Comment c WHERE c.post.id = : postId")
    List<Comment> getListCommentByPostId(Long postId);

    List<Comment> findByVerifiedIsNull();
}
