package com.dprol.post_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LikeDto {

    private Long id;

    @NotBlank
    private Long postId;

    @NotBlank
    private Long userId;

    @NotBlank
    private Long commentId;
}
