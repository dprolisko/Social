package com.dprol.achievement_service.service;

import com.dprol.achievement_service.dto.AchievementDto;
import com.dprol.achievement_service.entity.Achievement;
import com.dprol.achievement_service.entity.AchievementProgress;
import com.dprol.achievement_service.entity.User;
import com.dprol.achievement_service.exception.NotFoundException;
import com.dprol.achievement_service.mapper.AchievementMapper;
import com.dprol.achievement_service.repository.AchievementProgressRepository;
import com.dprol.achievement_service.repository.AchievementRepository;
import com.dprol.achievement_service.repository.UserRepository;
import com.dprol.achievement_service.service.achievement.AchievementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceImplTest {

    @Mock
    private AchievementRepository achievementRepository;
    @Mock
    private AchievementMapper achievementMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;

    @InjectMocks
    private AchievementServiceImpl service;

    private User user;
    private Achievement achievement;
    private AchievementDto achievementDto;
    private AchievementProgress progress;

    @BeforeEach
    void setUp() {

        user = User.builder().userId(1L).build();
        achievement = Achievement.builder().id(10L).title("Test").build();
        achievementDto = AchievementDto.builder().id(10L).title("Test").build();
        progress = AchievementProgress.builder().id(100L).build();
    }

    @Test
    void giveAchievement_ShouldSaveUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        service.giveAchievement(1L, achievement);

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void giveAchievement_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.giveAchievement(1L, achievement));

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void hasAchievement_ShouldReturnTrue_WhenExists() {
        when(achievementRepository.existsById(10L)).thenReturn(true);

        boolean result = service.hasAchievement(1L, 10L);

        assertTrue(result);
        verify(achievementRepository).existsById(10L);
    }

    @Test
    void hasAchievement_ShouldReturnFalse_WhenNotExists() {
        when(achievementRepository.existsById(10L)).thenReturn(false);

        boolean result = service.hasAchievement(1L, 10L);

        assertFalse(result);
        verify(achievementRepository).existsById(10L);
    }

    @Test
    void getAchievementProgress_ShouldReturnProgress_WhenFound() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(1L, 10L))
                .thenReturn(Optional.of(progress));

        AchievementProgress result = service.getAchievementProgress(1L, 10L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        verify(achievementProgressRepository).findByUserIdAndAchievementId(1L, 10L);
    }

    @Test
    void getAchievementProgress_ShouldThrow_WhenNotFound() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(1L, 10L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getAchievementProgress(1L, 10L));
    }

    @Test
    void createProgress_ShouldCallRepository() {
        service.createProgress(1L, 10L);

        verify(achievementProgressRepository).createProgress(1L, 10L);
    }

    @Test
    void findAchievementById_ShouldReturnDto_WhenFound() {
        when(achievementRepository.findById(10L)).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

        AchievementDto result = service.findAchievementById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(achievementRepository).findById(10L);
        verify(achievementMapper).toDto(achievement);
    }

    @Test
    void findAchievementById_ShouldThrow_WhenNotFound() {
        when(achievementRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.findAchievementById(10L));
    }

    @Test
    void findAchievementByTitle_ShouldReturnDto_WhenFound() {
        when(achievementRepository.findByTitle("Test")).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDto(achievement)).thenReturn(achievementDto);

        AchievementDto result = service.findAchievementByTitle("Test");

        assertNotNull(result);
        assertEquals("Test", result.getTitle());
        verify(achievementRepository).findByTitle("Test");
        verify(achievementMapper).toDto(achievement);
    }

    @Test
    void findAchievementByTitle_ShouldThrow_WhenNotFound() {
        when(achievementRepository.findByTitle("Test")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.findAchievementByTitle("Test"));
    }
}
