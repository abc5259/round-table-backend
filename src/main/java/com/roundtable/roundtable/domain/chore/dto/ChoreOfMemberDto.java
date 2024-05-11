package com.roundtable.roundtable.domain.chore.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.roundtable.roundtable.domain.schedule.Category;
import java.time.LocalDate;
import java.time.LocalTime;

public record ChoreOfMemberDto(
    Long choreId,
    String name,
    boolean isCompleted,
    LocalDate startDate,
    LocalTime startTime,
    Category category
) {
    @QueryProjection
    public ChoreOfMemberDto {
    }
}
