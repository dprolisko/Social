package com.dprol.social.controller.avatar;

import com.dprol.social.dto.user.UserProfileDto;
import com.dprol.social.service.user.avatar.AvatarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/avatar")

public class AvatarController {

    private final AvatarService avatarService;

    @PostMapping("/{userId}")
    @Operation(summary = "save profile picture")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {@ApiResponse (responseCode = "201", description = "Picture save")})
    public UserProfileDto saveProfilePicture(Long userId, MultipartFile file){
        return avatarService.saveProfilePicture(userId, file);
    }

    @DeleteMapping("/{userId}")
    public UserProfileDto deleteProfilePicture(Long userId){
        return avatarService.deleteProfilePicture(userId);
    }

    @GetMapping("/{userId}")
    public InputStreamResource getProfilePicture(Long userId){
        return avatarService.getProfilePicture(userId);
    }
}
