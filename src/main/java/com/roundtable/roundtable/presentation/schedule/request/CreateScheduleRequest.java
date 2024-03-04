package com.roundtable.roundtable.presentation.schedule.request;

import com.roundtable.roundtable.business.house.CreateScheduleDto;
import com.roundtable.roundtable.entity.schedule.DivisionType;
import com.roundtable.roundtable.entity.schedule.FrequencyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateScheduleRequest(
        @NotBlank(message = "name에 빈 값이 올 수 없습니다.")
        String name,
        @NotNull(message = "frequencyType에 빈 값이 올 수 없습니다.")
        FrequencyType frequencyType,
        @NotNull(message = "frequencyInterval에 빈 값이 올 수 없습니다.")
        @PositiveOrZero(message = "frequencyInterval은 0이상이어야 합니다.")
        Integer frequencyInterval,
        @NotNull(message = "startDate에 빈 값이 올 수 없습니다.")
        LocalDate startDate,
        @NotNull(message = "startTime에 빈 값이 올 수 없습니다.")
        LocalTime startTime,
        @NotNull(message = "divisionType에 빈 값이 올 수 없습니다.")
        DivisionType divisionType,
        @NotEmpty(message = "memberIds는 하나 이상이어야 합니다.")
        List<Long> memberIds,

        @NotNull(message = "categoryId에 빈 값이 올 수 없습니다.")
        Long categoryId
) {
    public CreateScheduleDto toCreateScheduleDto() {
        return new CreateScheduleDto(
                name,
                frequencyType,
                frequencyInterval,
                startDate,
                startTime
                ,divisionType
                ,memberIds,
                categoryId
        );
    }
}
