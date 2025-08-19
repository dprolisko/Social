package com.dprol.post_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String phone;

    private List<Long> subscriptions;

    private UserProfileDto userProfile;
}
