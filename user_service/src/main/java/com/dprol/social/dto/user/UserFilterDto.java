package com.dprol.social.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserFilterDto {

    @NotNull
    @Size(max = 32)
    private String username;

    @Size(max = 32)
    private String country;

    @Size(max = 32)
    private String city;

    private String contact;

    @Size(max = 64)
    private String email;

    @Size(max = 512)
    private String bio;

    @Size(max = 16)
    private String phone;

    private String skill;
}
