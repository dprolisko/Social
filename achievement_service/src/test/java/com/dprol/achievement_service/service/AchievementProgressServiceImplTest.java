package com.dprol.achievement_service.service;

import com.dprol.achievement_service.dto.AchievementProgressDto;
import com.dprol.achievement_service.entity.AchievementProgress;
import com.dprol.achievement_service.exception.NotFoundException;
import com.dprol.achievement_service.mapper.AchievementProgressMapper;
import com.dprol.achievement_service.repository.AchievementProgressRepository;
import com.dprol.achievement_service.service.achievement_progress.AchievementProgressServiceImpl;
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
class AchievementProgressServiceImplTest {

    @Mock
    private AchievementProgressRepository repository;
    @Mock
    private AchievementProgressMapper mapper;

    @InjectMocks
    private AchievementProgressServiceImpl service;

    private AchievementProgress progress;
    private AchievementProgressDto dto;
    private AchievementProgress progress2;
    private AchievementProgressDto dto2;

    @BeforeEach
    void setUp() {

        progress = AchievementProgress.builder().id(1L).build();
        dto = AchievementProgressDto.builder().id(1L).build();
        progress2 = AchievementProgress.builder().id(2L).build();
        dto2 = AchievementProgressDto.builder().id(2L).build();
    }

    @Test
    void createAchievementProgress_ShouldCallRepository() {
        service.createAchievementProgress(100L, 200L);

        verify(repository).createProgress(100L, 200L);
    }

    @Test
    void getAchievementProgress_ShouldReturnDto_WhenFound() {
        when(repository.findByUserIdAndAchievementId(100L, 200L)).thenReturn(Optional.of(progress));
        when(mapper.toDto(progress)).thenReturn(dto);

        AchievementProgressDto result = service.getAchievementProgress(100L, 200L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository).findByUserIdAndAchievementId(100L, 200L);
        verify(mapper).toDto(progress);
    }

    @Test
    void getAchievementProgress_ShouldThrowNotFound_WhenNotExists() {
        when(repository.findByUserIdAndAchievementId(100L, 200L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getAchievementProgress(100L, 200L));

        verify(repository).findByUserIdAndAchievementId(100L, 200L);
        verifyNoInteractions(mapper);
    }

    @Test
    void getAllAchievementProgress_ShouldReturnListOfDtos() {

        when(repository.findByUserId(100L)).thenReturn(List.of(progress, progress2));
        when(mapper.toDto(progress)).thenReturn(dto);
        when(mapper.toDto(progress2)).thenReturn(dto2);

        List<AchievementProgressDto> result = service.getAllAchievementProgress(100L);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(repository).findByUserId(100L);
        verify(mapper).toDto(progress);
        verify(mapper).toDto(progress2);
    }
}
