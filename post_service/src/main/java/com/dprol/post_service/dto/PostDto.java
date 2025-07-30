package com.dprol.post_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PostDto {

    private Long id;

    @NotBlank
    private Long authorId;

    @NotBlank
    @Size(max = 4096, message = "Content should be less then 4096 symbols")
    private String content;

    @NotBlank
    @Size(max = 32)
    private String authorName;
}
