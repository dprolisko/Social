package com.dprol.social.entity;

import com.dprol.social.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Jira")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Jira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
