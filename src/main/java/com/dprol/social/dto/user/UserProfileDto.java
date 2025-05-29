package com.dprol.social.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserProfileDto {

    @NotBlank
    private String fileId;

    @NotBlank
    private String smallFileId;
}
