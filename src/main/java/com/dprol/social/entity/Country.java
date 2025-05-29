package com.dprol.social.entity;

import com.dprol.social.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "country")

public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_name", length = 64, nullable = false)
    private String CountryName;

    @OneToMany(mappedBy = "country")
    private List<User> user;
}
