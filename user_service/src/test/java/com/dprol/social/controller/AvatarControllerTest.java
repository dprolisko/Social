package com.dprol.social.controller;

import com.dprol.social.controller.avatar.AvatarController;
import com.dprol.social.dto.user.UserProfileDto;
import com.dprol.social.service.user.avatar.AvatarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvatarControllerTest {

    @Mock
    private AvatarService avatarService;

    @InjectMocks
    private AvatarController avatarController;

    private final Long testUserId = 1L;
    private UserProfileDto testUserProfileDto;
    private MockMultipartFile testFile;

    @BeforeEach
    void setUp() {
        testUserProfileDto = new UserProfileDto();

        testFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }

    @Test
    void saveProfilePicture_ShouldReturnCreatedStatus() throws IOException {
        // Arrange
        when(avatarService.saveProfilePicture(anyLong(), any(MultipartFile.class)))
                .thenReturn(testUserProfileDto);

        // Act
        UserProfileDto result = avatarController.saveProfilePicture(testUserId, testFile);

        // Assert
        assertNotNull(result);
        verify(avatarService, times(1)).saveProfilePicture(testUserId, testFile);
    }

    @Test
    void deleteProfilePicture_ShouldReturnUserProfile() {
        // Arrange
        when(avatarService.deleteProfilePicture(anyLong()))
                .thenReturn(testUserProfileDto);

        // Act
        UserProfileDto result = avatarController.deleteProfilePicture(testUserId);

        // Assert
        assertNotNull(result);
        verify(avatarService, times(1)).deleteProfilePicture(testUserId);
    }

    @Test
    void getProfilePicture_ShouldReturnInputStreamResource() {
        // Arrange
        InputStreamResource mockResource = new InputStreamResource(
                new ByteArrayInputStream("test".getBytes())
        );
        when(avatarService.getProfilePicture(anyLong()))
                .thenReturn(mockResource);

        // Act
        InputStreamResource result = avatarController.getProfilePicture(testUserId);

        // Assert
        assertNotNull(result);
        verify(avatarService, times(1)).getProfilePicture(testUserId);
    }
}
