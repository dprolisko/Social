package com.dprol.post_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CommentDto {

    private Long id;

    @NotBlank
    private Long authorId;

    @NotBlank
    @Size(max = 32)
    private String authorName;

    @NotBlank
    @Size(max = 4096)
    private String content;

    //кол-во лайков, пост айди, дататайм
}
