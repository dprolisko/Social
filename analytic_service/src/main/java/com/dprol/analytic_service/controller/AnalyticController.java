package com.dprol.analytic_service.controller;

import com.dprol.analytic_service.dto.AnalyticDto;
import com.dprol.analytic_service.entity.Interval;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.service.AnalyticService;
import com.dprol.analytic_service.utils.EnumUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("analytic")
public class AnalyticController {

    private final AnalyticService analyticService;

    @GetMapping("/get")
    public List<AnalyticDto> getAnalytics(Long receiverId, String type, String interval, LocalDateTime from, LocalDateTime to) {
        Type type1 = EnumUtil.getEnum(Type.class, type);
        Interval interval1 = EnumUtil.getEnum(Interval.class, interval);
        return analyticService.getAllAnalytics(receiverId, type1, interval1, from, to);
    }
}
