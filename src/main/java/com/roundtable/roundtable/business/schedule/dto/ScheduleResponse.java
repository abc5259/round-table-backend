package com.roundtable.roundtable.business.schedule.dto;

import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleDto;
import java.time.LocalTime;

public record ScheduleResponse(
        Long id,
        String name,
        Category category,
        boolean isCompleted,
        LocalTime startTime,
        String managers,
        String extraManagers
) {
    public static ScheduleResponse from(ScheduleDto scheduleDto) {
        return new ScheduleResponse(
                scheduleDto.id(),
                scheduleDto.name(),
                scheduleDto.category(),
                scheduleDto.isCompleted(),
                scheduleDto.startTime(),
                scheduleDto.managers(),
                scheduleDto.extraManagers()
        );
    }
}
