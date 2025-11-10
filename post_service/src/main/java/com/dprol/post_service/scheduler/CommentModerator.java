package com.dprol.post_service.scheduler;

import com.dprol.post_service.config.ModerationConfig;
import com.dprol.post_service.entity.Comment;
import com.dprol.post_service.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor

public class CommentModerator {

    private final CommentRepository commentRepository;

    private final ModerationConfig moderationConfig;

    private final ExecutorService executorService;

    @Value("${moderation.batch-size}")
    private int batchSize;

    @Async("moderationExecutor")
    public void moderate(List<Comment> comments) {
        comments.forEach(comment -> {
            comment.setVerified(!moderationConfig.checkWord(comment.getContent()));
        comment.setVerifiedAt(LocalDateTime.now());
        commentRepository.save(comment);});
    }

    @Scheduled(cron = "${moderation.cron}")
    public void moderateContent() {
        List<Comment> comments = commentRepository.findByVerifiedIsNull();
        for (int i = 0; i < comments.size(); i+=batchSize) {
            List<Comment> subComments = comments.subList(i, Math.min(comments.size(), i + batchSize));
            CompletableFuture.runAsync(() -> {moderate(subComments);}, executorService);
        }
    }
}
