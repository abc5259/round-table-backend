package com.roundtable.roundtable.entity.schedule.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ScheduleMemberDetailDto(
        Long memberId,
        String name,
        Integer sequence
) {
    @QueryProjection
    public ScheduleMemberDetailDto {
    }
}
