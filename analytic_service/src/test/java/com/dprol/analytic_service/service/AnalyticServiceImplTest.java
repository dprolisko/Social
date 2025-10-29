package com.dprol.analytic_service.service;

import com.dprol.analytic_service.dto.AnalyticDto;
import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Interval;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.mapper.AnalyticMapper;
import com.dprol.analytic_service.repository.AnalyticRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticServiceImplTest {

    @Mock
    private AnalyticRepository analyticRepository;
    @Mock
    private AnalyticMapper analyticMapper;

    @InjectMocks
    private AnalyticServiceImpl analyticService;

    private Analytic analytic1;
    private Analytic analytic2;
    private AnalyticDto dto1;
    private AnalyticDto dto2;

    @BeforeEach
    void setUp() {

        analytic1 = Analytic.builder().id(1L).receiverId(2L).authorId(3L).type(Type.POST_VIEW).receivedAt(LocalDateTime.now()).build();
        analytic2 = Analytic.builder().id(2L).receiverId(3L).authorId(4L).type(Type.POST_VIEW).receivedAt(LocalDateTime.now()).build();

        dto1 = AnalyticDto.builder().id(1L).receiverId(2L).authorId(3L).type(Type.POST_VIEW).receiverAt(LocalDateTime.now()).build();
        dto2 = AnalyticDto.builder().id(2L).receiverId(3L).authorId(4L).type(Type.POST_VIEW).receiverAt(LocalDateTime.now()).build();
    }

    @Test
    void getAllAnalytics_WithInterval_ShouldFilterAndSort() {
        when(analyticRepository.findByReceiverId(100L, Type.POST_VIEW))
                .thenReturn(Stream.of(analytic1, analytic2));
        when(analyticMapper.toDto(analytic1)).thenReturn(dto1);
        when(analyticMapper.toDto(analytic2)).thenReturn(dto2);

        // допустим, Interval.DAY вернёт "сегодня минус 1 день"
        LocalDateTime threshold = LocalDateTime.now().minusDays(1);

            List<AnalyticDto> result = analyticService.getAllAnalytics(100L, Type.POST_VIEW, Interval.day, null, null);
            assertIterableEquals(List.of(dto1, dto2), result);

            InOrder inOrder = inOrder(analyticRepository, analyticMapper);
            inOrder.verify(analyticRepository).findByReceiverId(100L, Type.POST_VIEW);
            inOrder.verify(analyticMapper).toDto(analytic1);
            inOrder.verify(analyticMapper).toDto(analytic2);

    }

    @Test
    void getAllAnalytics_WithFromTo_ShouldFilterBetweenDates() {
        LocalDateTime from = LocalDateTime.now().minusDays(3);
        LocalDateTime to   = LocalDateTime.now();

        when(analyticRepository.findByReceiverId(100L, Type.POST_VIEW))
                .thenReturn(Stream.of(analytic1, analytic2));
        when(analyticMapper.toDto(analytic1)).thenReturn(dto1);
        when(analyticMapper.toDto(analytic2)).thenReturn(dto2);

        List<AnalyticDto> result = analyticService.getAllAnalytics(100L, Type.POST_VIEW, null, from, to);

        assertIterableEquals(List.of(dto1, dto2), result);

        InOrder inOrder = inOrder(analyticRepository, analyticMapper);
        inOrder.verify(analyticRepository).findByReceiverId(100L, Type.POST_VIEW);
        inOrder.verify(analyticMapper).toDto(analytic1);
        inOrder.verify(analyticMapper).toDto(analytic2);
    }


    @Test
    void saveAnalytic_ShouldCallRepositorySave() {
        analyticService.saveAnalytic(analytic1);

        verify(analyticRepository).save(analytic1);
    }
}
