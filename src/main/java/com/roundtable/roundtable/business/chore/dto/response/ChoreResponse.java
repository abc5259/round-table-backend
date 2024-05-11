package com.roundtable.roundtable.business.chore.dto.response;

import com.roundtable.roundtable.domain.chore.dto.ChoreMembersDetailDto;
import com.roundtable.roundtable.domain.schedule.Category;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ChoreResponse(
        Long choreId,
        String name,
        boolean isCompleted,
        LocalDate startDate,
        LocalTime startTime,
        List<String> memberNames,
        Category category
) {

    public static ChoreResponse create(ChoreMembersDetailDto choreMembersDetailDto) {
        return new ChoreResponse(
                choreMembersDetailDto.choreId(),
                choreMembersDetailDto.name(),
                choreMembersDetailDto.isCompleted(),
                choreMembersDetailDto.startDate(),
                choreMembersDetailDto.startTime(),
                List.of(choreMembersDetailDto.memberNames().split(",")),
                choreMembersDetailDto.category()
        );
    }
}
