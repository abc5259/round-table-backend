package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.entity.housework.HouseWorkCategory;
import com.roundtable.roundtable.entity.housework.HouseWorkDivision;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record CreateWeeklyHouseWork(
        String name,
        HouseWorkCategory houseWorkCategory,
        List<Long> dayIds,
        LocalDate activeDate,
        LocalTime assignedTime,
        HouseWorkDivision houseWorkDivision,
        int currSequence,
        int sequenceSize
) {
}
