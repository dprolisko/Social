package com.dprol.social.dto;

import com.dprol.social.entity.GoalStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GoalFilterDto {

    private Long goalId;

    private String goalName;

    private GoalStatus goalStatus;

    private LocalDateTime deadline;
}
