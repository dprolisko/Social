package com.dprol.social.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "ContentData")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ContentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private byte[] content;
}
