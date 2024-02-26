package com.roundtable.roundtable.entity.chore.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.roundtable.roundtable.entity.category.dto.CategoryDetailV1Dto;
import java.time.LocalDate;
import java.time.LocalTime;

public record ChoreMembersDetailDto(
        Long choreId,
        String name,
        boolean isCompleted,
        LocalDate startDate,
        LocalTime startTime,
        String memberNames,
        CategoryDetailV1Dto category
) {

    @QueryProjection
    public ChoreMembersDetailDto {
    }
}
