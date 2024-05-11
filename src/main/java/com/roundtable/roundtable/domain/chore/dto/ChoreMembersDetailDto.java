package com.roundtable.roundtable.domain.chore.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.roundtable.roundtable.domain.schedule.Category;
import java.time.LocalDate;
import java.time.LocalTime;

public record ChoreMembersDetailDto(
        Long choreId,
        String name,
        boolean isCompleted,
        LocalDate startDate,
        LocalTime startTime,
        String memberNames,
        Category category
) {

    @QueryProjection
    public ChoreMembersDetailDto {
    }
}
