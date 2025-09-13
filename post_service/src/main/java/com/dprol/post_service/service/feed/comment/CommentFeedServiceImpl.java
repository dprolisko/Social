package com.dprol.post_service.service.feed.comment;

import com.dprol.post_service.dto.feed.CommentFeedDto;
import com.dprol.post_service.entity.Comment;
import com.dprol.post_service.mapper.CommentMapper;
import com.dprol.post_service.repository.CommentRepository;
import com.dprol.post_service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CommentFeedServiceImpl implements CommentFeedService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final UserService userService;

    @Override
    public List<CommentFeedDto> getCommentFeed(Long postId) {
        List<Comment> comment = commentRepository.getListCommentByPostId(postId);
        return comment.stream().map(comment1 -> {userService.getUserById(comment1.getAuthorId());
        return commentMapper.toComment(comment1);}).toList();
    }
}
