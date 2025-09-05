package com.dprol.post_service.service.comment;

import com.dprol.post_service.dto.CommentDto;
import com.dprol.post_service.entity.Comment;
import com.dprol.post_service.kafka.event.Status;
import com.dprol.post_service.kafka.producer.CommentProducer;
import com.dprol.post_service.mapper.CommentMapper;
import com.dprol.post_service.repository.CommentRepository;
import com.dprol.post_service.validator.comment.CommentValidator;
import com.dprol.post_service.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final CommentValidator commentValidator;

    private final CommentProducer commentProducer;

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        commentValidator.validateCommentById(commentDto.getId());
        Comment comment = commentMapper.toEntity(commentDto);
        commentRepository.save(comment);
        sendToKafka(comment, Status.created);
        return commentMapper.toDto(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentValidator.validateCommentById(commentId);
        Comment comment = commentRepository.findById(commentId).orElse(null);
        commentRepository.delete(comment);
        sendToKafka(comment, Status.deleted);
    }

    @Override
    public CommentDto updateComment(Long commentId) {
        Comment commentValid = findCommentById(commentId);
        commentValidator.validateCommentByVerificationStatus(commentValid);
        commentRepository.save(commentValid);
        sendToKafka(commentValid, Status.updated);
        return commentMapper.toDto(commentValid);
    }

    @Override
    public List<CommentDto> getListCommentByUserId(Long userId) {
        List<Comment> comments =  commentRepository.getListCommentByUserId(userId);
        return comments.stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(String.format("Comment with id %s not found", commentId)));
    }

    @Override
    public void verifyComment(List<Comment> comments) {

    }

    private void sendToKafka(Comment comment, Status status) {
        if (comment.getAuthorId() != null) {
            commentProducer.produce(commentMapper.toKafkaEvent(comment, status));
        }
    }
}
