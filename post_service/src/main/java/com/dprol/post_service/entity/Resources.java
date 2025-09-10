package com.dprol.post_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.resource.ResourceType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "resources")

public class Resources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "key", nullable = false)
    private String key;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "publishedAt")
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ResourceType resourceType;

    @Column(name = "title", length = 32, nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;
}
