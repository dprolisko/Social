package com.dprol.post_service.service.like;

import com.dprol.post_service.dto.like.CommentLikeDto;
import com.dprol.post_service.entity.like.CommentLike;
import com.dprol.post_service.kafka.event.Status;
import com.dprol.post_service.kafka.producer.like.CommentLikeProducer;
import com.dprol.post_service.mapper.like.CommentLikeMapper;
import com.dprol.post_service.repository.like.CommentLikeRepository;
import com.dprol.post_service.validator.like.LikeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    private final CommentLikeMapper commentLikeMapper;

    private final LikeValidator likeValidator;

    private final CommentLikeProducer commentLikeProducer;

    @Override
    public CommentLikeDto addCommentLike(Long commentLikeId, Long userId) {
        likeValidator.validateCommentLike(commentLikeId);
        CommentLikeDto commentLikeDto = new CommentLikeDto();
        commentLikeDto.setId(commentLikeId);
        commentLikeDto.setUserId(userId);
        CommentLike commentLike = commentLikeMapper.toEntity(commentLikeDto);
        commentLikeRepository.save(commentLike);
        commentLikeProducer.produce(commentLikeMapper.toKafkaEvent(commentLike, Status.created));
        return commentLikeMapper.toDto(commentLike);
    }

    @Override
    public void deleteCommentLike(Long commentLikeId, Long userId) {
        CommentLike commentLike = commentLikeRepository.findById(commentLikeId).orElse(null);
        commentLikeRepository.deleteById(commentLikeId);
        commentLikeProducer.produce(commentLikeMapper.toKafkaEvent(commentLike,Status.deleted));
    }
}
