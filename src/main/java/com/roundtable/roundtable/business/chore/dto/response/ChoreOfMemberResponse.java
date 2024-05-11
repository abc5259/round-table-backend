package com.roundtable.roundtable.business.chore.dto.response;

import com.roundtable.roundtable.domain.chore.dto.ChoreOfMemberDto;
import com.roundtable.roundtable.domain.schedule.Category;
import java.time.LocalDate;
import java.time.LocalTime;

public record ChoreOfMemberResponse(
        Long choreId,
        String name,
        boolean isCompleted,
        LocalDate startDate,
        LocalTime startTime,
        Category category
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
