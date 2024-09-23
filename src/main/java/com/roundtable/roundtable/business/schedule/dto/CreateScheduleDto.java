package com.roundtable.roundtable.business.schedule.dto;

import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateScheduleDto(
    String name,
    LocalDate startDate,
    LocalTime startTime,
    DivisionType divisionType,
    ScheduleType scheduleType,
    List<Long> memberIds,
    Category category,
    List<Day> days
) {
}
