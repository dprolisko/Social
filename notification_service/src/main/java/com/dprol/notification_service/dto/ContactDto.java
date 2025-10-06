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

public class ContactDto {

    private Long id;

    private Long userId;

    private Contact contact;
}
