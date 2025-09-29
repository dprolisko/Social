package com.dprol.notification_service.dto;

import com.dprol.notification_service.entity.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String phone;

    private Contact contact;
}
