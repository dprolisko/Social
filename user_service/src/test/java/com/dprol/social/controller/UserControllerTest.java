package com.dprol.social.controller;

import com.dprol.social.controller.user.UserController;
import com.dprol.social.dto.user.UserDto;
import com.dprol.social.service.user.UserService;
import com.dprol.social.service.user.avatar.AvatarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AvatarService avatarService;

    @InjectMocks
    private UserController userController;

    private UserDto testUserDto;
    private final Long testUserId = 1L;

    @BeforeEach
    void setUp() {
        testUserDto = new UserDto();
        testUserDto.setId(testUserId);
        // установите другие необходимые поля
    }

    @Test
    void createUser_ShouldGenerateAvatarAndReturnCreatedUser() {
        // Arrange
       // when(avatarService.generatedPicture(any(UserDto.class))).thenReturn(testUserDto);
        when(userService.createUser(any(UserDto.class))).thenReturn(testUserDto);

        // Act
        UserDto result = userController.createUser(testUserDto);

        // Assert
        assertNotNull(result);
        assertEquals(testUserId, result.getId());
        verify(avatarService, times(1)).generatedPicture(testUserDto);
        verify(userService, times(1)).createUser(testUserDto);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        // Arrange
        when(userService.updateUser(any(UserDto.class))).thenReturn(testUserDto);

        // Act
        UserDto result = userController.updateUser(testUserDto);

        // Assert
        assertNotNull(result);
        assertEquals(testUserId, result.getId());
        verify(userService, times(1)).updateUser(testUserDto);
    }

    @Test
    void deleteUser_ShouldReturnDeletedUser() {
        // Arrange
        when(userService.deleteUser(anyLong())).thenReturn(testUserDto);

        // Act
        UserDto result = userController.deleteUser(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(testUserId, result.getId());
        verify(userService, times(1)).deleteUser(testUserId);
    }

    @Test
    void getUserById_WithSingleId_ShouldReturnUser() {
        // Arrange
        when(userService.getUserById(anyLong())).thenReturn(testUserDto);

        // Act
        UserDto result = userController.getUserById(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(testUserId, result.getId());
        verify(userService, times(1)).getUserById(testUserId);
    }

    @Test
    void getUserById_WithListOfIds_ShouldReturnListOfUsers() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<UserDto> userDtos = Arrays.asList(
                new UserDto(1L, "User1"),
                new UserDto(2L, "User2"),
                new UserDto(3L, "User3")
        );

        when(userService.getUserById(anyList())).thenReturn(userDtos);

        // Act
        List<UserDto> result = userController.getUserById(ids);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(userService, times(1)).getUserById(ids);
    }
}