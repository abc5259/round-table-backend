package com.roundtable.roundtable.domain.schedule.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ScheduleIdDto(
        Long id
) {
    @QueryProjection
    public ScheduleIdDto {
    }
}
