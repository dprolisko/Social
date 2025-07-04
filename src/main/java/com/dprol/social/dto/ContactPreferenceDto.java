package com.dprol.social.dto;

import com.dprol.social.entity.contact.ContactPreference;
import com.dprol.social.entity.contact.PreferedContactType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder

public class ContactPreferenceDto {

    private Long id;

    private Long userId;

    private PreferedContactType preferedContactType;
}
