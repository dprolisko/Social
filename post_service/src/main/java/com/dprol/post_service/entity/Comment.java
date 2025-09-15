package com.dprol.post_service.entity;

import com.dprol.post_service.entity.like.PostLike;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comment")

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "authorId", nullable = false)
    private Long authorId;

    @Column(name = "authorName", nullable = false, length = 32, unique = true)
    private String authorName;

    @Column(name = "content", nullable = false, length = 4096)
    private String content;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @OneToMany
    @JoinColumn(name = "comment_likeId")
    private List<PostLike> like;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "publishedAt")
    private LocalDateTime publishedAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "published")
    private boolean published;

    @Column(name = "verified")
    private boolean verified;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "verifiedAt")
    private LocalDateTime verifiedAt;
}
