package com.dprol.social.dto;

import com.dprol.social.entity.contact.PreferedContactType;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder

public class ContactPreferenceDto {

    private Long id;

    private Long userId;

    private PreferedContactType preferedContactType;
}
