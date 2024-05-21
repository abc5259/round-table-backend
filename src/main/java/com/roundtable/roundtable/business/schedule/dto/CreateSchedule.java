package com.roundtable.roundtable.business.schedule.dto;

import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateSchedule(
        String name,
        LocalDate startDate,
        LocalTime startTime,
        DivisionType divisionType,
        List<Long> memberIds,
        Category category,
        List<Integer> dayIds
) {
}
