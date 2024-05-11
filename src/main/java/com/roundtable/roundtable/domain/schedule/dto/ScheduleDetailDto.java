package com.roundtable.roundtable.domain.schedule.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Frequency;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ScheduleDetailDto(
        Long scheduleId,
        String name,
        Frequency frequency,
        LocalDate startDate,
        LocalTime startTime,
        DivisionType divisionType,
        Category category,
        List<ScheduleMemberDetailDto> allocators
) {

    @QueryProjection
    public ScheduleDetailDto(Long scheduleId,
                             String name,
                             Frequency frequency,
                             LocalDate startDate,
                             LocalTime startTime,
                             DivisionType divisionType,
                             Category category) {
        this(scheduleId, name, frequency, startDate, startTime, divisionType, category, null);
    }

    public ScheduleDetailDto withAllocators(List<ScheduleMemberDetailDto> allocators) {
        return new ScheduleDetailDto(
                this.scheduleId(),
                this.name(),
                this.frequency(),
                this.startDate(),
                this.startTime(),
                this.divisionType(),
                this.category(),
                allocators);
    }
}

