package com.roundtable.roundtable.implement.schedule;

import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.schedule.DivisionType;
import com.roundtable.roundtable.entity.schedule.FrequencyType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateSchedule(
        String name,
        FrequencyType frequencyType,
        Integer frequencyInterval,
        LocalDate startDate,
        LocalTime startTime,
        DivisionType divisionType,
        List<Long> memberIds,
        Category category
) {
}
