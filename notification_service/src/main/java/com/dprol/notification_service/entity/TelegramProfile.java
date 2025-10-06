package com.dprol.notification_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "telegram_profile")
@Builder

public class TelegramProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotNull
    @Column(name = "userId")
    private Long userId;

    @NotNull
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "telegramId")
    private Long telegramId;

    @NotNull
    @Column(name = "isActive")
    @ColumnDefault(value = "false")
    private boolean isActive;
}
