package com.dprol.social.entity.user;

import jakarta.persistence.Embeddable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable

public class UserProfile {

    private String fileId;

    private String smallFileId;
}
