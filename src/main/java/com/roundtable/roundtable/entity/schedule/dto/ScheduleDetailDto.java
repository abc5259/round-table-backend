package com.roundtable.roundtable.entity.schedule.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.roundtable.roundtable.entity.schedule.DivisionType;
import com.roundtable.roundtable.entity.schedule.Frequency;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public record ScheduleDetailDto(
        Long scheduleId,
        String name,
        Frequency frequency,
        LocalDate startDate,
        LocalTime startTime,
        DivisionType divisionType,
        CategoryDetailDto category,
        List<ScheduleMemberDetailDto> allocators
) {

    public record CategoryDetailDto(
            Long categoryId,
            String name,
            Integer point
    ) {
        @QueryProjection
        public CategoryDetailDto {
        }
    }

    @QueryProjection
    public ScheduleDetailDto(Long scheduleId,
                             String name,
                             Frequency frequency,
                             LocalDate startDate,
                             LocalTime startTime,
                             DivisionType divisionType,
                             Long categoryId,
                             String categoryName,
                             Integer categoryPoint) {
        this(
                scheduleId,
                name,
                frequency,
                startDate,
                startTime,
                divisionType,
                new CategoryDetailDto(
                        categoryId,
                        categoryName,
                        categoryPoint
                ),
                new ArrayList<>());
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

