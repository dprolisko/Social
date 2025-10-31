package com.dprol.analytic_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "analytic")

public class Analytic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receiver_id", nullable = false, unique = true)
    private Long receiverId;

    @Column(name = "author_id", nullable = false, unique = true)
    private Long authorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "interval")
    private Interval interval;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "received_at")
    private LocalDateTime receivedAt;
}
