package com.roundtable.roundtable.domain.schedule.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.roundtable.roundtable.domain.schedule.Category;
import java.time.LocalTime;

public record ScheduleOfMemberDto(
        Long id,
        String name,
        Category category,
        boolean isCompleted,
        LocalTime startTime
) {
    @QueryProjection
    public ScheduleOfMemberDto {
    }
}
