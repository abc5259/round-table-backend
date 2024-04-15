package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.business.schedule.dto.CreateSchedule;
import com.roundtable.roundtable.domain.category.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.FrequencyType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateScheduleDto(
    String name,
    FrequencyType frequencyType,
    Integer frequencyInterval,
    LocalDate startDate,
    LocalTime startTime,
    DivisionType divisionType,
    List<Long> memberIds,
    Long categoryId) {
    public CreateSchedule toCreateSchedule(Category category) {
        return new CreateSchedule(
                name,
                frequencyType,
                frequencyInterval,
                startDate,
                startTime
                ,divisionType
                ,memberIds,
                category
        );
    }
}
