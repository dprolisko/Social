package com.dprol.post_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserProfileDto {

    private String smallFileId;

    private String fileId;
}
