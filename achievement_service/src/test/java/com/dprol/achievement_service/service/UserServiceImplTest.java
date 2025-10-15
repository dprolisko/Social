package com.dprol.achievement_service.service;

import com.dprol.achievement_service.dto.AchievementDto;
import com.dprol.achievement_service.dto.UserDto;
import com.dprol.achievement_service.entity.Achievement;
import com.dprol.achievement_service.entity.User;
import com.dprol.achievement_service.exception.NotFoundException;
import com.dprol.achievement_service.mapper.AchievementMapper;
import com.dprol.achievement_service.mapper.UserMapper;
import com.dprol.achievement_service.repository.UserRepository;
import com.dprol.achievement_service.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AchievementMapper achievementMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private AchievementDto achievementDto;
    private Achievement achievement;
    private UserDto userDto1;
    private UserDto userDto2;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {

        user = User.builder().userId(1L).build();
        achievementDto = AchievementDto.builder().id(10L).title("Test Achievement").build();
        achievement = Achievement.builder().id(10L).title("Test Achievement").build();
        userDto1 = UserDto.builder().id(1L).build();
        userDto2 = UserDto.builder().id(2L).build();
        user1 = User.builder().userId(1L).build();
        user2 = User.builder().userId(2L).build();
    }

    @Test
    void giveAchievement_ShouldSaveUserWithAchievement_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(achievementMapper.toEntity(achievementDto)).thenReturn(achievement);

        userService.giveAchievement(1L, achievementDto);

        verify(userRepository).findById(1L);
        verify(achievementMapper).toEntity(achievementDto);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void giveAchievement_ShouldThrowNotFound_WhenUserNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.giveAchievement(1L, achievementDto));

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void hasAchievement_ShouldReturnTrue_WhenRepositoryReturnsTrue() {
        when(userRepository.existsByUserId(1L, 10L)).thenReturn(true);

        boolean result = userService.hasAchievement(1L, 10L);

        assertTrue(result);
        verify(userRepository).existsByUserId(1L, 10L);
    }

    @Test
    void hasAchievement_ShouldReturnFalse_WhenRepositoryReturnsFalse() {
        when(userRepository.existsByUserId(1L, 10L)).thenReturn(false);

        boolean result = userService.hasAchievement(1L, 10L);

        assertFalse(result);
        verify(userRepository).existsByUserId(1L, 10L);
    }

    @Test
    void getAchievementsByUserId_ShouldReturnMappedDtos() {

        when(userRepository.findByUserId(1L)).thenReturn(List.of(user1, user2));
        when(userMapper.toDto(user1)).thenReturn(userDto1);
        when(userMapper.toDto(user2)).thenReturn(userDto2);

        List<UserDto> result = userService.getAchievementsByUserId(1L);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(userRepository).findByUserId(1L);
        verify(userMapper).toDto(user1);
        verify(userMapper).toDto(user2);
    }
}
