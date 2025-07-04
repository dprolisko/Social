package com.dprol.social.service.user.avatar;

import com.dprol.social.dto.user.UserDto;
import com.dprol.social.dto.user.UserProfileDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {

    UserProfileDto saveProfilePicture(Long userId, MultipartFile file);

    UserProfileDto deleteProfilePicture(Long userId);

    InputStreamResource getProfilePicture(Long userId);

    void generatedPicture(UserDto userDto);
}