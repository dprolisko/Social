package com.dprol.social.entity.contact;

import com.dprol.social.entity.contact.PreferedContactType;
import com.dprol.social.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "contact_preference")

public class ContactPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prefered_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PreferedContactType preferedContactType;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
