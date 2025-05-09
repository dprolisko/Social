package com.dprol.social.dto;

import com.dprol.social.entity.ContactPreference;
import com.dprol.social.entity.PreferedContactType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ContactPreferenceDto {

    private Long id;

    private Long userId;

    @NotBlank
    private ContactPreference preferedContactType;
}
