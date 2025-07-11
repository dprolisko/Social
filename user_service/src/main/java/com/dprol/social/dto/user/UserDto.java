package com.dprol.social.dto.user;

import com.dprol.social.entity.Country;
import com.dprol.social.entity.contact.Contact;
import com.dprol.social.entity.user.UserProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserDto {

    private Long id;

    @NotBlank
    @Size(max = 32, message = "Username should be less then 33 symbols")
    private String username;

    @NotBlank
    @Size(max = 32, message = "Password should be less then 33 symbols")
    private String password;

    @NotBlank
    @Size(max = 64, message = "Email should be less then 65 symbols")
    private String email;

    private UserProfile userprofile;

    @NotBlank
    @Size(max = 16, message = "Phone should be less then 17 symbols")
    private String phone;

    private Country country;

    private Contact contact;

    private boolean active;

    public UserDto(long l, String user3) {
    }

    public UserDto(Long id, String email, String username, Object o) {
    }
}
