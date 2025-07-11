package com.dprol.social.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.dprol.social.dto.user.UserDto;
import com.dprol.social.dto.user.UserProfileDto;
import com.dprol.social.entity.user.User;
import com.dprol.social.entity.user.UserProfile;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.mapper.user.UserProfileMapper;
import com.dprol.social.repository.UserRepository;
import com.dprol.social.service.amazonS3.S3Service;
import com.dprol.social.service.user.avatar.AvatarServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvatarServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileMapper userProfileMapper;

    @Mock
    private S3Service s3Service;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AvatarServiceImpl avatarService;

    // Тестовые данные
    private final Long USER_ID = 1L;
    private final String BUCKET = "test-bucket";
    private final String SMALL_KEY = "small-avatar.jpg";
    private final String LARGE_KEY = "large-avatar.jpg";
    private final User user = new User();
    private final UserProfile userProfile = new UserProfile(SMALL_KEY, LARGE_KEY);
    private final MockMultipartFile file = new MockMultipartFile(
            "avatar",
            "avatar.jpg",
            "image/jpeg",
            "test-image".getBytes()
    );

    // ------------------------ saveProfilePicture() ------------------------

    @Test
    void saveProfilePicture_Success() throws IOException {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userProfileMapper.toDto(any())).thenReturn(new UserProfileDto());

        // Действие
        UserProfileDto result = avatarService.saveProfilePicture(USER_ID, file);

        // Проверка
        assertNotNull(result);
        verify(s3Service, times(2)).uploadFile(eq(BUCKET), anyString(), any());
        verify(userRepository).save(user);
        assertNotNull(user.getUserProfile());
    }

    @Test
    void saveProfilePicture_UserNotFound() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        assertThrows(UserNotFoundException.class,
                () -> avatarService.saveProfilePicture(USER_ID, file));
    }

    @Test
    void saveProfilePicture_UploadFails() throws IOException {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        doThrow(AmazonS3Exception.class).when(s3Service).uploadFile(any(), any(), any());

        // Действие + Проверка
        assertThrows(RuntimeException.class,
                () -> avatarService.saveProfilePicture(USER_ID, file));
    }

    // ------------------------ deleteProfilePicture() ------------------------

    @Test
    void deleteProfilePicture_Success() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userProfileMapper.toDto(any())).thenReturn(new UserProfileDto());

        // Действие
        UserProfileDto result = avatarService.deleteProfilePicture(USER_ID);

        // Проверка
        assertNotNull(result);
        verify(s3Service, times(2)).deleteFile(eq(BUCKET), anyString());
        verify(userRepository).save(user);
        assertNull(user.getUserProfile().getFileId());
        assertNull(user.getUserProfile().getSmallFileId());
    }

    @Test
    void deleteProfilePicture_DeleteFails() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        doThrow(AmazonS3Exception.class).when(s3Service).deleteFile(any(), any());

        // Действие + Проверка
        assertThrows(AmazonS3Exception.class,
                () -> avatarService.deleteProfilePicture(USER_ID));
    }

    // ------------------------ getProfilePicture() ------------------------

    @Test
    void getProfilePicture_Success() throws IOException {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(s3Service.getFile(BUCKET, LARGE_KEY)).thenReturn(new S3Object());

        // Действие
        InputStreamResource result = avatarService.getProfilePicture(USER_ID);

        // Проверка
        assertNotNull(result);
        assertTrue(result.contentLength() > 0);
    }

    @Test
    void getProfilePicture_NoProfileSet() {
        // Подготовка
        user.getUserProfile().setFileId(null);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        // Действие + Проверка
        assertThrows(DataValidationException.class,
                () -> avatarService.getProfilePicture(USER_ID));
    }

    // ------------------------ generatedPicture() ------------------------

    @Test
    void generatedPicture_Success() {
        // Подготовка
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        byte[] imageBytes = "image-data".getBytes();

        when(restTemplate.getForObject(anyString(), eq(byte[].class)))
                .thenReturn(imageBytes);

        // Действие
        avatarService.generatedPicture(userDto);

        // Проверка
        verify(s3Service).uploadFile(eq(BUCKET), anyString(), any());
        assertNotNull(userDto.getUserprofile());
    }

    @Test
    void generatedPicture_FailedGeneration() {
        // Подготовка
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");

        when(restTemplate.getForObject(anyString(), eq(byte[].class)))
                .thenReturn(null);

        // Действие + Проверка
        assertThrows(DataValidationException.class,
                () -> avatarService.generatedPicture(userDto));
    }

    @Test
    void generatedPicture_RetryOnFailure() {
        // Подготовка
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");

        // Первые 3 вызова бросают исключение, 4-й успешен
        when(restTemplate.getForObject(anyString(), eq(byte[].class)))
                .thenThrow(RestClientException.class)
                .thenThrow(RestClientException.class)
                .thenThrow(RestClientException.class)
                .thenReturn("success".getBytes());

        // Действие
        avatarService.generatedPicture(userDto);

        // Проверка
        verify(restTemplate, times(4)).getForObject(anyString(), eq(byte[].class));
        verify(s3Service).uploadFile(any(), any(), any());
    }
}