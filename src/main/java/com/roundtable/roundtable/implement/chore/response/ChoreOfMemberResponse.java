package com.roundtable.roundtable.implement.chore.response;

import com.roundtable.roundtable.entity.category.dto.CategoryDetailV1Dto;
import com.roundtable.roundtable.entity.chore.dto.ChoreOfMemberDto;
import java.time.LocalDate;
import java.time.LocalTime;

public record ChoreOfMemberResponse(
        Long choreId,
        String name,
        boolean isCompleted,
        LocalDate startDate,
        LocalTime startTime,
        CategoryDetailV1Dto category
) {
    public static ChoreOfMemberResponse create(ChoreOfMemberDto choreOfMemberDto) {
        return new ChoreOfMemberResponse(
                choreOfMemberDto.choreId(),
                choreOfMemberDto.name(),
                choreOfMemberDto.isCompleted(),
                choreOfMemberDto.startDate(),
                choreOfMemberDto.startTime(),
                choreOfMemberDto.category()
        );
    }
}
