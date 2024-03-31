package com.roundtable.roundtable.entity.schedule.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ScheduleIdDto(
        Long id
) {
    @QueryProjection
    public ScheduleIdDto {
    }
}
