package com.dprol.social.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Premium")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Premium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn (name = "user_id")
    private User user;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column (name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column (name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;
}
