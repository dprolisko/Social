package com.dprol.social.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PremiumDto {

    private Long premiumId;

    private Long userId;

    private int period;

    private LocalDateTime startTime;

    public PremiumDto(Long validPremiumId, String active) {
    }
}
