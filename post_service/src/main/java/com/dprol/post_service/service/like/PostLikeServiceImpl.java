package com.dprol.post_service.service.like;

import com.dprol.post_service.dto.like.PostLikeDto;
import com.dprol.post_service.entity.like.PostLike;
import com.dprol.post_service.kafka.event.Status;
import com.dprol.post_service.kafka.producer.like.PostLikeProducer;
import com.dprol.post_service.mapper.like.PostLikeMapper;
import com.dprol.post_service.repository.like.PostLikeRepository;
import com.dprol.post_service.validator.like.LikeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;

    private final PostLikeMapper postLikeMapper;

    private final LikeValidator likeValidator;

    private final PostLikeProducer postLikeProducer;

    @Override
    public PostLikeDto addPostLike(Long postLikeId, Long userId) {
        likeValidator.validatePostLike(postLikeId);
        PostLikeDto postLikeDto = new PostLikeDto();
        postLikeDto.setId(postLikeId);
        postLikeDto.setUserId(userId);
        PostLike postLike = postLikeMapper.toEntity(postLikeDto);
        postLikeRepository.save(postLike);
        postLikeProducer.produce(postLikeMapper.toKafkaEvent(postLike, Status.created));
        return postLikeMapper.toDto(postLike);
    }

    @Override
    public void deletePostLike(Long postLikeId, Long userId) {
        PostLike postLike = postLikeRepository.findById(postLikeId).orElse(null);
        postLikeRepository.deleteById(postLikeId);
        postLikeProducer.produce(postLikeMapper.toKafkaEvent(postLike, Status.deleted));
    }
}
