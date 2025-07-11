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

    public SkillDto(Long id, String skillName, Long id1) {
    }

    public SkillDto(long id, String name, String description) {
    }
}
