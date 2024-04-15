package com.roundtable.roundtable.business.schedule.dto;

import com.roundtable.roundtable.domain.category.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.FrequencyType;
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
