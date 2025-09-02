package com.dprol.post_service.entity;

import com.dprol.post_service.entity.like.PostLike;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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
@Table(name = "post")

public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "authorId", nullable = false)
    private Long authorId;

    @Column(name = "authorName", nullable = false, length = 32, unique = true)
    private String authorName;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "content", nullable = false, length = 4096)
    private String content;

    @Column(name = "published")
    private boolean published;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "publishedAt")
    private LocalDateTime publishedAt;

    @Column(name = "viewCount", nullable = false)
    @ColumnDefault("0")
    private Long viewCount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "verifiedAt")
    private LocalDateTime verifiedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEntity postStatus;

    @OneToMany(mappedBy = "post")
    private List<Resources> resources;

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<Hashtag> hashtags;
}
