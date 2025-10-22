package com.dprol.analytic_service.entity;

import java.time.LocalDateTime;

public enum Interval {
    year, month, weekend, day;
    public static LocalDateTime toLocalDateTime(Interval interval) {
        return switch (interval){
            case year -> LocalDateTime.now().minusYears(1);
            case month -> LocalDateTime.now().minusMonths(1);
            case weekend -> LocalDateTime.now().minusWeeks(1);
            case day -> LocalDateTime.now().minusDays(1);
        };
    }
}
