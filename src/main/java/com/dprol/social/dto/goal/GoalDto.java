package com.dprol.social.dto.goal;

import com.dprol.social.entity.goal.GoalStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GoalDto {

    private Long id;

    @NotBlank
    private List<Long> userId;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private LocalDateTime deadline;

    @NotBlank
    private GoalStatus status;
}
