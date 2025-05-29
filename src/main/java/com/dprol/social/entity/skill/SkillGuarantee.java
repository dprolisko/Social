package com.dprol.social.entity.skill;

import com.dprol.social.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "SkillGuarantee")
@Builder

public class SkillGuarantee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn (name = "userguarentee_id")
    private User userGuarantee;

    @ManyToOne
    @JoinColumn (name = "skill_id")
    private Skill skill;
}
