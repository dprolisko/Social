package com.dprol.analytic_service.dto;

import com.dprol.analytic_service.entity.Type;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder

public class AnalyticDto {

    private Long id;

    @NotNull
    private Long receiverId;

    @NotNull
    private Long authorId;

    @NotNull
    private Type type;

    @NotNull
    private LocalDateTime receiverAt;
}
