package com.dprol.analytic_service.service;

import com.dprol.analytic_service.dto.AnalyticDto;
import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Interval;
import com.dprol.analytic_service.entity.Type;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticService {

    List<AnalyticDto> getAllAnalytics(Long receiverId, Type type, Interval interval, LocalDateTime from, LocalDateTime to);

    void saveAnalytic(Analytic analytic);
}
