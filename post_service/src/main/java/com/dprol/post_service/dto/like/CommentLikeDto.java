package com.dprol.post_service.dto.like;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CommentLikeDto {

    private Long id;

    @NotBlank
    private Long postId;

    @NotBlank
    private Long userId;

    @NotBlank
    private Long commentId;
}
