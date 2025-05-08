package com.dprol.social.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "skills")
@Getter
@Setter
@Builder

public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skillName", length = 32, nullable = false)
    private String skillName;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(name = "users_skills", joinColumns = @JoinColumn(name="skill_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> user;

    @OneToMany(mappedBy = "user")
    private List<SkillGuarantee> skillGuarantee;
}
