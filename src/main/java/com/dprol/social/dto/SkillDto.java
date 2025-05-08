package com.dprol.social.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SkillDto {

    private Long id;

    @NotBlank
    private String skillName;
}
