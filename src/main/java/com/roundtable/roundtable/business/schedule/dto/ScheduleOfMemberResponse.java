package com.roundtable.roundtable.business.schedule.dto;

import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleOfMemberDto;
import java.time.LocalTime;

public record ScheduleOfMemberResponse(
        Long id,
        String name,
        Category category,
        boolean isCompleted,
        LocalTime startTime
) {

    public static ScheduleOfMemberResponse from(ScheduleOfMemberDto scheduleOfMemberDto) {
        return new ScheduleOfMemberResponse(
                scheduleOfMemberDto.id(),
                scheduleOfMemberDto.name(),
                scheduleOfMemberDto.category(),
                scheduleOfMemberDto.isCompleted(),
                scheduleOfMemberDto.startTime()
        );
    }
}
