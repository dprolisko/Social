package com.dprol.social.entity.event;

import com.dprol.social.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Events")

public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 16)
    private String name;

    @Column(name = "description", length = 256)
    private String description;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "end")
    private LocalDateTime end;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create")
    private LocalDateTime created;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update")
    private LocalDateTime updated;

    @ManyToOne
    private User user;

    @Column(name = "count", nullable = false)
    private int count;

    @ManyToMany
    private List<User> userList;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
}
