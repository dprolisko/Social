package com.dprol.social.entity.contact;

import com.dprol.social.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table (name ="contact")

public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contact", length = 128, unique = true)
    private String contact;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
