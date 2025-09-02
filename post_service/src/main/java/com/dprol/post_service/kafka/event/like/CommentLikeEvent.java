package com.dprol.post_service.kafka.event.like;

import com.dprol.post_service.kafka.event.KafkaEvent;
import com.dprol.post_service.kafka.event.Status;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized

public class CommentLikeEvent implements KafkaEvent {

    private Long commentId;

    private Long userId;

    private Status status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime likeTime;
}
