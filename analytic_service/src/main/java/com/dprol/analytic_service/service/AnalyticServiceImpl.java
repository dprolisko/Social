package com.dprol.analytic_service.service;

import com.dprol.analytic_service.dto.AnalyticDto;
import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Interval;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.mapper.AnalyticMapper;
import com.dprol.analytic_service.repository.AnalyticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor

public class AnalyticServiceImpl implements AnalyticService {

    private final AnalyticRepository analyticRepository;

    private final AnalyticMapper analyticMapper;

    @Override
    public List<AnalyticDto> getAllAnalytics(Long receiverId, Type type, Interval interval, LocalDateTime from, LocalDateTime to) {
        Stream<Analytic> streamAnalytic = analyticRepository.findByReceiverId(receiverId, type);
        if (interval != null) {
            LocalDateTime date = Interval.toLocalDateTime(interval);
            streamAnalytic = streamAnalytic.filter(analytic -> analytic.getReceivedAt().isAfter(date));
        }
        else{
            streamAnalytic = streamAnalytic.filter(analytic -> analytic.getReceivedAt().isAfter(from) && analytic.getReceivedAt().isBefore(to));
        }
        return streamAnalytic.sorted(Comparator.comparing(Analytic::getReceivedAt)).map(analyticMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void saveAnalytic(Analytic analytic) {
        analyticRepository.save(analytic);
    }
}
